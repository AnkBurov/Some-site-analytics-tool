package io.zooobserver.repository

import io.zooobserver.api.Thread
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ThreadRepository : JpaRepository<Thread, Long>