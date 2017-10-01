package io.zooobserver.adapter

import io.zooobserver.annotation.AllOpen
import io.zooobserver.api.BoardDto
import io.zooobserver.api.FullThreadDto
import io.zooobserver.api.Thread
import io.zooobserver.util.bodyNotNull
import io.zooobserver.util.ok
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@AllOpen
@Component
class ApiAdapter {

    private val restTemplate = RestTemplate()

    suspend fun getThread(url: String): Thread {
        val boardThread = restTemplate.getForEntity(url, FullThreadDto::class.java)
                .ok("Response from $url is not 200")
                .bodyNotNull()
                .apply { assert(threads.isNotEmpty()) { "Thread $title doesn't contain posts" } }
        return convertFullThreadDto(boardThread)
    }

    suspend fun getBoardPages(url: String): List<String> {
        return restTemplate.getForEntity(url, BoardDto::class.java)
                .ok("Response from $url is not 200")
                .bodyNotNull()
                .pages
                .map { if (it == 0) "index" else it.toString() }
                .let { it.subList(0, it.size - 2) }
    }

    suspend fun getThreadNumbersFromBoardPage(url: String, pattern: Regex): List<String> {
        return restTemplate.getForEntity(url, BoardDto::class.java)
                .ok("Response from $url is not 200")
                .bodyNotNull()
                .threads
                .filter {
                    it.posts[0].subject.contains(pattern)
                }
                .map { it.threadNumber }
    }

    private fun convertFullThreadDto(fullThread: FullThreadDto) =
            Thread(id = fullThread.threadId,
                    title = fullThread.title,
                    board = fullThread.board,
                    posts = fullThread.threads[0].posts)
}