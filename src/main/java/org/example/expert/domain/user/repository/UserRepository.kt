package org.example.expert.domain.user.repository

import org.example.expert.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByNickname(nickname: String): Optional<User>
    fun existsByEmail(email: String): Boolean
}
