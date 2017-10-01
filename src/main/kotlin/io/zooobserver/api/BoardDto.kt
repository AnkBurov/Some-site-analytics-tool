package io.zooobserver.api

import com.fasterxml.jackson.annotation.JsonProperty

class BoardDto(@JsonProperty("Board")
               val board: String,
               val pages: List<Int>,
               val threads: List<BoardThreadDto>) {
}