package boki.board.comment.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

// @SoftDelete(columnName = "deleted") // 삭제처리를 하는 경우도 있고, 직접 삭제하는 경우도 있기 때문에 비활성화
@Table(name = "comment")
@Entity
class Comment protected constructor(
    @Id
    val commentId: Long? = null,
    var content: String,
    val parentCommentId: Long,
    val articleId: Long, // shared key
    val writerId: Long,
    var deleted: Boolean = false,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            commentId: Long,
            content: String,
            parentCommentId: Long?,
            articleId: Long,
            writerId: Long,
        ): Comment {
            return Comment(
                commentId = commentId,
                content = content,
                parentCommentId = parentCommentId ?: commentId,
                articleId = articleId,
                writerId = writerId,
                createdAt = LocalDateTime.now(),
            )
        }
    }

    val isRoot: Boolean
        get() = parentCommentId == commentId

    fun delete() {
        deleted = true
    }
}