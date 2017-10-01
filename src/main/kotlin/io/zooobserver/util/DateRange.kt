package io.zooobserver.util

import java.sql.Timestamp
import java.util.*

data class DateRange(val startDate: Date, val endDate: Date) {
    constructor(startDate: Long, endDate: Long) : this(Date(startDate), Date(endDate))
}

fun minutesToMilliseconds(minutes: Int) = minutes * 60 * 1000

/**
 * Split date range to smaller date ranges with the specified interval (in minutes)
 */
fun DateRange.split(interval: Int): List<DateRange> {
    val result = arrayListOf<DateRange>()
    val intervalMilliSeconds = minutesToMilliseconds(interval)
    var time = this.startDate.time
    while (time < endDate.time) {
        var endTimeInterval = time + intervalMilliSeconds - 1
        if (endTimeInterval > endDate.time) endTimeInterval = endDate.time
        result.add(DateRange(Date(time), Date(endTimeInterval)))
        time += intervalMilliSeconds
    }
    return result
}

operator fun DateRange.contains(timestamp: Timestamp): Boolean {
    return timestamp > this.startDate && timestamp < this.endDate
}