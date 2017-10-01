package io.zooobserver.service

import io.zooobserver.api.Thread

interface ThreadPersistenceService {

    fun saveThread(thread: Thread): Thread
}