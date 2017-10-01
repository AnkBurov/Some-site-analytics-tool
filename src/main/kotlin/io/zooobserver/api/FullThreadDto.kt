package io.zooobserver.api

import com.fasterxml.jackson.annotation.JsonProperty

class FullThreadDto(@JsonProperty("current_thread")
                     val threadId: Long,

                    @JsonProperty("Board")
                     val board: String,

                    val title: String,

                    val threads: MutableList<ThreadDto>)