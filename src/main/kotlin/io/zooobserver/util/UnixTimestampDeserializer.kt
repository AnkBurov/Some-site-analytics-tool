package io.zooobserver.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

import java.sql.Timestamp

class UnixTimestampDeserializer : JsonDeserializer<Timestamp>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Timestamp {
        val timestamp = jp.text.trim()
        return Timestamp(java.lang.Long.valueOf(timestamp)!! * 1000)
    }
}
