package org.example.expert.domain.todo.service

import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: AuthUser, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = User.fromAuthUser(authUser)

        val weather = weatherClient.todayWeather

        val newTodo = Todo(
            todoSaveRequest.title,
            todoSaveRequest.contents,
            weather,
            user
        )
        val savedTodo = todoRepository.save(newTodo)

        return TodoSaveResponse(
            savedTodo.id!!,
            savedTodo.title,
            savedTodo.contents,
            weather,
            UserResponse(user.id, user.email)
        )
    }

    fun getTodos(
        page: Int,
        size: Int,
        weather: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)

        val todos = todoRepository.findAllByOrderByModifiedAtDesc(weather, startDate, endDate, pageable)

        return todos.map<TodoResponse> { todo: Todo ->
            TodoResponse(
                todo.id!!,
                todo.title,
                todo.contents,
                todo.weather,
                UserResponse(todo.user.id, todo.user.email),
                todo.createdAt,
                todo.modifiedAt
            )
        }
    }

    fun searchTodos(
        page: Int, size: Int, title: String?, startDate: LocalDateTime?,
        endDate: LocalDateTime?, nickname: String?
    ): Page<TodoSearchResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)

        return todoRepository.searchTodos(title, startDate, endDate, nickname, pageable)
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow { InvalidRequestException("Todo not found") }

        val user: User = todo.user

        return TodoResponse(
            todo.id!!,
            todo.title,
            todo.contents,
            todo.weather,
            UserResponse(user.id, user.email),
            todo.createdAt,
            todo.modifiedAt
        )
    }
}
