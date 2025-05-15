package org.example.expert.domain.common.entity

import jakarta.persistence.*

@Entity
@Table(name = "log")
class Log(var entityName: String,
          var action: String) : Timestamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
