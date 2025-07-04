package org.example.expert.domain.comment.service

import org.example.expert.domain.comment.dto.request.CommentSaveRequest
import org.example.expert.domain.comment.dto.response.CommentResponse
import org.example.expert.domain.comment.dto.response.CommentSaveResponse
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.comment.repository.CommentRepository
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService (
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository
) {

    @Transactional
    fun saveComment(authUser: AuthUser, todoId: Long, commentSaveRequest: CommentSaveRequest): CommentSaveResponse {
        val user = userRepository.findById(authUser.id).orElseThrow { InvalidRequestException("User not found") }
        val todo = todoRepository.findById(todoId).orElseThrow { InvalidRequestException("Todo not found") }

        val newComment = Comment(
            commentSaveRequest.contents,
            user,
            todo
        )

        val savedComment = commentRepository.save(newComment)

        return CommentSaveResponse(
            savedComment.id!!,
            savedComment.contents,
            UserResponse(user.id!!, user.email)
        )
    }

    fun getComments(todoId: Long): List<CommentResponse> {
        val commentList = commentRepository.findByTodoIdWithUser(todoId)

        val dtoList: MutableList<CommentResponse> = ArrayList()
        for (comment in commentList) {
            val user: User = comment.user
            val dto = CommentResponse(
                comment.id!!,
                comment.contents,
                UserResponse(user.id!!, user.email)
            )
            dtoList.add(dto)
        }
        return dtoList
    }
}
