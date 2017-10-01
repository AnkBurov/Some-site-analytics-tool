package io.zooobserver.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.zooobserver.util.parseHtmlText
import io.zooobserver.util.UnixTimestampDeserializer
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Post(@JsonProperty("num")
                @Id
                val id: Long,

                val op: String,

                var subject: String,

                val parent: String,

                val date: String,

                var banned: String,
                @JsonDeserialize(using = UnixTimestampDeserializer::class)
                val timestamp: Timestamp,

                val lasthit: String,

                var likes: String,

                var name: String,

                var dislikes: String,

                @Column(length = 20000)
                var comment: String) {
    init {
        subject = subject.parseHtmlText()
        name = name.parseHtmlText()
        if (name == "Аноним") {
            name = "# OP"
        }
        comment = comment.parseHtmlText()
    }
}