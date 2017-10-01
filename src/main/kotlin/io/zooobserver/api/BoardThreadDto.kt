package io.zooobserver.api

import com.fasterxml.jackson.annotation.JsonProperty

class BoardThreadDto(@JsonProperty("thread_num")
                     val threadNumber: String,
                     val posts: List<Post>)