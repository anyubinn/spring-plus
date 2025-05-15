package org.example.expert.domain.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupRequest (
    @field:NotBlank
    @field:Email
    var email: String,
    @field:NotBlank
    var password: String,
    @field:NotBlank
    var nickname: String,
    @field:NotBlank
    var userRole: String
)