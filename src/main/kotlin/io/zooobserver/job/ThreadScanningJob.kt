package io.zooobserver.job

import io.zooobserver.annotation.AllOpen
import io.zooobserver.service.ApiSelectionService
import io.zooobserver.service.ThreadPersistenceService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@AllOpen
@Service
class ThreadScanningJob(val apiSelectionService: ApiSelectionService,
                        val threadPersistenceService: ThreadPersistenceService,
                        @Value("\${board.url}") val boardUrl: String) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "\${cron.pattern}")
    fun execute() {
        logger.debug("Job started")
        apiSelectionService.getThreadsFromBoard(boardUrl)
                .forEach {
                    logger.info("Saving ${it.title} thread")
                    threadPersistenceService.saveThread(it)
                }
        logger.debug("Job ended")
    }
}