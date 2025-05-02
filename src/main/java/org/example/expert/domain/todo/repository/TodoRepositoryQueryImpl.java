package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.example.expert.domain.todo.entity.QTodo;


@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    private final QTodo todo = QTodo.todo;
    private final QUser user = QUser.user;
    private final QManager manager = QManager.manager;
    private final QComment comment = QComment.comment;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(String title, LocalDateTime startDate, LocalDateTime endDate, String nickname, Pageable pageable) {

        List<TodoSearchResponse> result = queryFactory
                .select(new QTodoSearchResponse(
                        todo.title,
                        manager.id.countDistinct(),
                        comment.id.countDistinct()
                ))
                .from(todo)
                .where(
                        titleContains(title),
                        startDateGte(startDate),
                        endDateLte(endDate),
                        nicknameContains(user, nickname)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        titleContains(title),
                        startDateGte(startDate),
                        endDateLte(endDate)
                ).fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression titleContains(String title) {

        if (title != null) {
            return todo.title.contains(title);
        }

        return null;
    }

    private BooleanExpression startDateGte(LocalDateTime startDate) {

        if (startDate != null) {
            return todo.createdAt.goe(startDate);
        }

        return null;
    }

    private BooleanExpression endDateLte(LocalDateTime endDate) {

        if (endDate != null) {
            return todo.createdAt.loe(endDate);
        }

        return null;
    }

    private BooleanExpression nicknameContains(QUser user, String nickname) {

        if (nickname != null) {
            return user.nickname.contains(nickname);
        }

        return null;
    }
}
