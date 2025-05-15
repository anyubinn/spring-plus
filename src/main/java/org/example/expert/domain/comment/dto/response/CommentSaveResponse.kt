package org.example.expert.domain.comment.dto.response

import org.example.expert.domain.user.dto.response.UserResponse

class CommentSaveResponse(
    val id: Long,
    val contents: String,
    val user: UserResponse
)
