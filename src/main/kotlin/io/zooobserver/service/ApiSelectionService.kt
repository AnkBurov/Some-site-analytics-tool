package io.zooobserver.service

import io.zooobserver.api.Thread

interface ApiSelectionService {

    fun getThread(url: String): Thread

    fun getThreadsFromBoard(url: String): List<Thread>
}