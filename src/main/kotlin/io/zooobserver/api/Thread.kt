package io.zooobserver.api

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Thread(@Id
                  val id: Long,

                  val title: String,

                  val board: String,

                  @OneToMany
                  val posts: MutableList<Post>)