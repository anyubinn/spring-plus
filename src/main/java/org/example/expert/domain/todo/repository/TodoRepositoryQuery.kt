package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface TodoRepositoryQuery {
    fun findByIdWithUser(todoId: Long): Optional<Todo>

    fun searchTodos(
        title: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        nickname: String?,
        pageable: Pageable
    ): Page<TodoSearchResponse>
}
