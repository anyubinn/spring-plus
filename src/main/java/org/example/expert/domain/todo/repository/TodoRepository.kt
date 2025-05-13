package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TodoRepository : JpaRepository<Todo, Long>, TodoRepositoryQuery {
    @EntityGraph(attributePaths = ["user"])
    @Query("SELECT t FROM Todo t WHERE (:weather IS NULL OR t.weather = :weather) AND (:startDate IS NULL OR t.modifiedAt >= :startDate) AND (:endDate IS NULL OR t.modifiedAt <= :endDate) ORDER BY t.modifiedAt DESC")
    fun findAllByOrderByModifiedAtDesc(
        @Param("weather") weather: String?,
        @Param("startDate") startDate: LocalDateTime?,
        @Param("endDate") endDate: LocalDateTime?,
        pageable: Pageable
    ): Page<Todo>
}
