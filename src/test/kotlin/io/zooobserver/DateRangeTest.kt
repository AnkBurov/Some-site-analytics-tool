package io.zooobserver

import io.zooobserver.util.DateRange
import io.zooobserver.util.split
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateRangeTest {

    @Test
    fun split() {
        val dateRange = DateRange("01.01.2017 15:00:00".asDate(), "01.01.2017 16:00:00".asDate())
        val firstDate = "01.01.2017 15:00:00".asDate()
        val secondDate = "01.01.2017 15:15:00".asDate()
        val thirdDate = "01.01.2017 15:30:00".asDate()
        val fourthDate = "01.01.2017 15:45:00".asDate()
        val expectedDateIntervals = arrayListOf(
                DateRange(firstDate, Date(secondDate.time - 1)),
                DateRange(secondDate, Date(thirdDate.time - 1)),
                DateRange(thirdDate, Date(fourthDate.time - 1)),
                DateRange(fourthDate, Date(dateRange.endDate.time - 1)))
        val intervalMinutes = 15

        dateRange.split(intervalMinutes)
                .let { assertEquals(expectedDateIntervals, it) }
    }

    @Test
    fun whatever() {
        val contains = "Российский Крым и Новороссия №7903 ".contains(Regex("[Н,н,H][o,о]в[o,о][р,p][o,о][c,с]"))
        println(contains)
    }

    private fun String.asDate() = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(this)
}