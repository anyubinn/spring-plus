package org.example.expert.config

import at.favre.lib.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {
    fun encode(rawPassword: String): String {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray())
    }

    fun matches(rawPassword: String, encodedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword)
        return result.verified
    }
}
