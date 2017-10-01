package io.zooobserver.service

import io.zooobserver.adapter.ApiAdapter
import io.zooobserver.annotation.AllOpen
import io.zooobserver.api.Thread
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@AllOpen
@Service
class ApiSelectionServiceImpl(val apiAdapter: ApiAdapter, @Value("\${custom.pattern}") val regexString: String) : ApiSelectionService {
    override fun getThread(url: String) = runBlocking { apiAdapter.getThread(url) }

    override fun getThreadsFromBoard(url: String): List<Thread> {
        return runBlocking {
            val result = arrayListOf<Thread>()
            val pattern = Regex(regexString)
            apiAdapter.getBoardPages("$url/index.json")
                    .map { boardPage ->
                        async(CommonPool) {
                            apiAdapter.getThreadNumbersFromBoardPage("$url/$boardPage.json", pattern)
                                    .forEach { threadNumber ->
                                        val thread = apiAdapter.getThread("$url/res/$threadNumber.json")
                                        result.add(thread)
                                    }
                        }
                    }.forEach { it.await() }
            result
        }
    }
}