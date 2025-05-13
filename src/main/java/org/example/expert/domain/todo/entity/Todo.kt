package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Entity
@Table(name = "todos")
class Todo(
    val title: String,
    val contents: String,
    val weather: String,
    @field:JoinColumn(name = "user_id", nullable = false)
    @field:ManyToOne(fetch = FetchType.LAZY)
    val user: User
) : Timestamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    private val comments: List<Comment> = ArrayList()

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.PERSIST])
    private val managers: MutableList<Manager> = ArrayList()

    init {
        managers.add(Manager(user, this))
    }
}
