package org.example.expert.domain.user.entity

import jakarta.persistence.*
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.user.enums.UserRole

@Entity
@Table(name = "users", indexes = [Index(name = "nickname_index", columnList = "nickname")])
class User (
    @Column(unique = true)
    var email: String,
    var password: String,
    var nickname: String,
    @Enumerated(EnumType.STRING)
    var userRole: UserRole
) : Timestamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var profileImage: String? = null

    fun changePassword(password: String) {
        this.password = password
    }

    fun updateRole(userRole: UserRole) {
        this.userRole = userRole
    }

    fun updateProfileImage(profileImage: String?) {
        this.profileImage = profileImage
    }
}
