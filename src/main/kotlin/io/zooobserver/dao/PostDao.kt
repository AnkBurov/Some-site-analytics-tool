package io.zooobserver.dao

import io.zooobserver.util.DateRange
import io.zooobserver.util.timestamp
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import javax.sql.DataSource
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.PreparedStatementSetter
import java.sql.Timestamp

/**
 * Dao for custom queries for Post entity
 */
@Component
class PostDao(val dataSource: DataSource) {

    private val jdbcTemplate = JdbcTemplate(dataSource)

    suspend fun getPostCountsGroupedByDate(dateRange: DateRange): Map<Timestamp, Int> {
        return jdbcTemplate.query(
                "SELECT POST.TIMESTAMP, count(ID) FROM POST WHERE POST.TIMESTAMP > ? AND POST.TIMESTAMP < ? GROUP BY POST.TIMESTAMP ORDER BY POST.TIMESTAMP ASC",
                PreparedStatementSetter { preparedStatement ->
                    preparedStatement.setTimestamp(1, dateRange.startDate.timestamp())
                    preparedStatement.setTimestamp(2, dateRange.endDate.timestamp())
                },
                ResultSetExtractor<Map<Timestamp, Int>> { resultSet ->
                    val map = hashMapOf<Timestamp, Int>()
                    while (resultSet.next()) {
                        val timestamp = resultSet.getTimestamp(1)
                        val count = resultSet.getInt(2)
                        map.put(timestamp, count)
                    }
                    map
                }
        )
    }

    suspend fun getUniquePostersGroupedByDate(dateRange: DateRange): List<Triple<String, Long, Timestamp>> {
        return jdbcTemplate.query("""
            select POST.NAME, THREAD_POSTS.THREAD_ID, POST.TIMESTAMP
            from POST
            join THREAD_POSTS on POST.ID = THREAD_POSTS.POSTS_ID
            where POST.TIMESTAMP > ? and POST.TIMESTAMP < ?
            order by POST.TIMESTAMP ASC;
        """.trimIndent(),
                PreparedStatementSetter { preparedStatement ->
                    preparedStatement.setTimestamp(1, dateRange.startDate.timestamp())
                    preparedStatement.setTimestamp(2, dateRange.endDate.timestamp())
                },
                ResultSetExtractor<List<Triple<String, Long, Timestamp>>> { resultSet ->
                    val result = arrayListOf<Triple<String, Long, Timestamp>>()
                    while (resultSet.next()) {
                        val posterName = resultSet.getString(1)
                        val threadId = resultSet.getLong(2)
                        val timestamp = resultSet.getTimestamp(3)
                        result.add(Triple(posterName, threadId, timestamp))
                    }
                    result
                })
    }
}