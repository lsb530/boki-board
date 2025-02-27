package boki.board.comment.repository

import boki.board.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    @Query(
        value = "SELECT COUNT(*) FROM (" +
                " SELECT comment_id FROM comment " +
                " WHERE article_id = :articleId AND parent_comment_id = :parentCommentId" +
                " LIMIT :limit" +
                ") c",
        nativeQuery = true
    )
    fun countBy(
        @Param("articleId") articleId: Long,
        @Param("parentCommentId") parentCommentId: Long?,
        @Param("limit") limit: Long,
    ): Long
}