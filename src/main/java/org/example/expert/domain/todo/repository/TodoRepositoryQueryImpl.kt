package org.example.expert.domain.todo.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.expert.domain.comment.entity.QComment
import org.example.expert.domain.manager.entity.QManager
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResponse
import org.example.expert.domain.todo.entity.QTodo
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.QUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class TodoRepositoryQueryImpl(
    private val queryFactory: JPAQueryFactory
) : TodoRepositoryQuery {

    private val todo: QTodo = QTodo.todo
    private val user: QUser = QUser.user
    private val manager: QManager = QManager.manager
    private val comment: QComment = QComment.comment

    override fun findByIdWithUser(todoId: Long): Optional<Todo> {
        val result = queryFactory
            .selectFrom(todo)
            .leftJoin(todo.user, user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchOne()

        return Optional.ofNullable(result)
    }

    override fun searchTodos(
        title: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        nickname: String?,
        pageable: Pageable
    ): Page<TodoSearchResponse> {
        val result = queryFactory
            .select(
                QTodoSearchResponse(
                    todo.title,
                    manager.id.countDistinct(),
                    comment.id.countDistinct()
                )
            )
            .from(todo)
            .where(
                titleContains(title),
                startDateGte(startDate),
                endDateLte(endDate),
                nicknameContains(user, nickname)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .leftJoin(todo.managers, manager)
            .leftJoin(manager.user, user)
            .leftJoin(todo.comments, comment)
            .groupBy(todo.id)
            .orderBy(todo.createdAt.desc())
            .fetch()

        val total = queryFactory
            .select(todo.count())
            .from(todo)
            .where(
                titleContains(title),
                startDateGte(startDate),
                endDateLte(endDate)
            ).fetchOne() ?: 0L

        return PageImpl(result, pageable, total)
    }

    private fun titleContains(title: String?): BooleanExpression? {
        if (title != null) {
            return todo.title.contains(title)
        }

        return null
    }

    private fun startDateGte(startDate: LocalDateTime?): BooleanExpression? {
        if (startDate != null) {
            return todo.createdAt.goe(startDate)
        }

        return null
    }

    private fun endDateLte(endDate: LocalDateTime?): BooleanExpression? {
        if (endDate != null) {
            return todo.createdAt.loe(endDate)
        }

        return null
    }

    private fun nicknameContains(user: QUser, nickname: String?): BooleanExpression? {
        if (nickname != null) {
            return user.nickname.contains(nickname)
        }

        return null
    }
}
