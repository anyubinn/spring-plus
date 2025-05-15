package org.example.expert.domain.todo.controller

import org.example.expert.config.GlobalExceptionHandler
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.service.TodoService
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.enums.UserRole
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(TodoController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class)
internal class TodoControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var todoService: TodoService

    @Test
    fun todo_단건_조회에_성공한다() {
        // given
        val todoId = 1L
        val title = "title"
        val authUser = AuthUser(1L, "email", "nickname", UserRole.USER)
        val userResponse = UserResponse(1L, "email")
        val now = LocalDateTime.of(2025, 5, 14, 12, 0)

        val response = TodoResponse(
            todoId,
            title,
            "contents",
            "Sunny",
            userResponse,
            now,
            now
        )

        Mockito.`when`(todoService.getTodo(todoId)).thenReturn(response)

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/{todoId}", todoId))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(todoId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
    }

    @Test
    fun todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() {
        val todoId = 1L

        Mockito.`when`(todoService.getTodo(todoId))
            .thenThrow(InvalidRequestException("Todo not found"))

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/{todoId}", todoId))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Todo not found"))
    }
}
