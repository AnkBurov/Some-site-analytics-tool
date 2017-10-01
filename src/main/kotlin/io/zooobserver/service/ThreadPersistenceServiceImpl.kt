package io.zooobserver.service

import io.zooobserver.annotation.AllOpen
import io.zooobserver.api.Thread
import io.zooobserver.repository.PostRepository
import io.zooobserver.repository.ThreadRepository
import io.zooobserver.util.updatePosts
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@AllOpen
@Service
class ThreadPersistenceServiceImpl(val threadRepository: ThreadRepository,
                                   val postRepository: PostRepository) : ThreadPersistenceService {

    @Transactional
    override fun saveThread(thread: Thread): Thread {
        val persistedThread : Thread? = threadRepository.findOne(thread.id)
        if (persistedThread != null) {
            persistedThread.updatePosts(thread.posts)
            postRepository.save(persistedThread.posts)
            return persistedThread
        } else {
            postRepository.save(thread.posts)
            return threadRepository.save(thread)
        }
    }
}