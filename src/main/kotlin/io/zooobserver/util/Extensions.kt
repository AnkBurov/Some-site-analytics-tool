package io.zooobserver.util

import io.zooobserver.api.DataPoint
import io.zooobserver.api.Post
import io.zooobserver.api.Thread
import io.zooobserver.controller.ChartController
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest

fun <T> ResponseEntity<T?>.ok(message: String? = null): ResponseEntity<T?> {
    assertEquals(message, HttpStatus.OK, this.statusCode)
    return this
}

fun <T> ResponseEntity<T?>.bodyNotNull(message: String? = null) = this.body ?: throw AssertionError(message)

fun String.parseHtmlText(): String = Jsoup.parse(this).text()

fun Thread.updatePosts(posts: Collection<Post>) {
    val groupedPostsById = this.posts.groupBy { it.id }
    posts.forEach {
        val persistedPost: Post? = groupedPostsById[it.id]?.get(0)
        if (persistedPost != null) {
            with(persistedPost) {
                if (it.banned != banned) banned = it.banned
                if (it.likes != likes) likes = it.likes
                if (it.dislikes != dislikes) dislikes = it.dislikes
            }
        } else {
            this.posts.add(it)
        }
    }
}

fun Date.timestamp() = Timestamp(this.time)

fun List<DataPoint>.trim() = this.trim { it.y == 0 }

/**
 * Trim list with predicate
 */
inline fun <T> List<T>.trim(predicate: (T) -> Boolean): List<T> {
    var startIndex = 0
    var endIndex = this.size - 1
    var startFound = false

    while (startIndex <= endIndex) {
        val index = if (!startFound) startIndex else endIndex
        val match = predicate(this[index])

        if (!startFound) {
            if (!match)
                startFound = true
            else
                startIndex += 1
        } else {
            if (!match)
                break
            else
                endIndex -= 1
        }
    }

    return this.subList(startIndex, endIndex + 1)
}

inline suspend fun List<DateRange>.transformToDataPoints(crossinline yValueCallBack: (DateRange) -> Int): List<DataPoint> {
    return this.map { dateInterval ->
        async(CommonPool) {
            DataPoint(ChartController.formatter.format(dateInterval.startDate), yValueCallBack(dateInterval))
        }
    }.map { it.await() }
            .trim()
}

fun HttpServletRequest.getClientIpAddress(): String {
    var ipAddress = this.getHeader("X-FORWARDED-FOR")
    if (ipAddress == null) {
        ipAddress = this.remoteAddr
    }
    return ipAddress
}