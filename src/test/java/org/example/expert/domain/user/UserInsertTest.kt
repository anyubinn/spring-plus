package org.example.expert.domain.user

import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.enums.UserRole
import org.example.expert.domain.user.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserInsertTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun insertMillionUsers() {
        val totalUsers = 1000000
        val batchSize = 10000
        val nicknameSet: MutableSet<String> = HashSet()
        val buffer: MutableList<User> = ArrayList(batchSize)
        val targetNickname = "target_nickname_1234"

        for (i in 1..totalUsers) {
            var nickname: String
            do {
                nickname = "user_" + UUID.randomUUID().toString().substring(0, 8)
            } while (!nicknameSet.add(nickname))

            if (i == 777777) {
                nickname = targetNickname
            }

            val user = User("user$i@test.com", "1234", nickname, UserRole.USER)
            buffer.add(user)

            if (i % batchSize == 0) {
                userRepository.saveAll(buffer)
                userRepository.flush()
                buffer.clear()
                println("$i user insert success")
            }
        }

        if (!buffer.isEmpty()) {
            userRepository.saveAll(buffer)
            userRepository.flush()
        }

        println("million user insert success")
    }
}
