package boki.board.comment.service.response

import boki.board.comment.entity.Comment
import java.time.LocalDateTime

data class CommentDetailResponse(
    val commentId: Long,
    val content: String,
    val parentCommentId: Long,
    val articleId: Long,
    val writerId: Long,
    val deleted: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(comment: Comment): CommentDetailResponse {
            return CommentDetailResponse(
                commentId = comment.commentId!!,
                content = comment.content,
                parentCommentId = comment.parentCommentId,
                articleId = comment.articleId,
                writerId = comment.writerId,
                deleted = comment.deleted,
                createdAt = comment.createdAt,
            )
        }
    }
}