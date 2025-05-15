package org.example.expert.domain.common.dto

import org.example.expert.domain.user.enums.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUser(
    val id: Long,
    val email: String,
    val nickname: String,
    val userRole: UserRole
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority("ROLE_" + userRole.name))
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return email
    }
}
