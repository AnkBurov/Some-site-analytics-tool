package io.zooobserver.controller

import io.zooobserver.annotation.AllOpen
import io.zooobserver.api.MultiChartDataPoint
import io.zooobserver.dao.PostDao
import io.zooobserver.util.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest

@AllOpen
@RestController
@RequestMapping("/api")
class ChartController(val postDao: PostDao) {

    @GetMapping
    fun getPostsChart(@RequestParam startDate: Long, @RequestParam endDate: Long, request: HttpServletRequest): MultiChartDataPoint {
        val dateRange = DateRange(startDate, endDate)
        logger.info("GET request chart controller with dateRage $dateRange from IP address ${request.getClientIpAddress()}")
        checkMaxDateRange(dateRange, MAX_DAYS_RANGE)

        return runBlocking {
            val firstChartAsync = async(CommonPool) {
                val postCountsGroupedByDate = postDao.getPostCountsGroupedByDate(dateRange)
                dateRange.split(INTERVAL_MINUTES)
                        .transformToDataPoints { dateInterval ->
                            postCountsGroupedByDate.filter { it.key in dateInterval }
                                    .values
                                    .count()
                        }
            }

            val secondChartAsync = async(CommonPool) {
                val threadPostersGroupedByDate = postDao.getUniquePostersGroupedByDate(dateRange)
                dateRange.split(INTERVAL_MINUTES)
                        .transformToDataPoints { dateInterval ->
                            threadPostersGroupedByDate
                                    .groupBy { it.first }
                                    .mapValues { it.value.filter { it.third in dateInterval } }
                                    .filter { it.value.isNotEmpty() }
                                    .count()
                        }
            }
            MultiChartDataPoint(firstChartAsync.await(), secondChartAsync.await())
        }
    }

    private fun checkMaxDateRange(dateRange: DateRange, maxDays: Int) {
        with(dateRange) {
            if (TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS) > maxDays) {
                throw IllegalArgumentException("Difference between $startDate and $endDate cannot be more than $maxDays days")
            }
        }
    }

    companion object {
        const val MAX_DAYS_RANGE = 30
        const val INTERVAL_MINUTES = 15
        const val PATTERN = "yyyy-MM-dd HH:mm"
        val formatter = SimpleDateFormat(PATTERN)
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}