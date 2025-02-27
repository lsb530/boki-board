package boki.board.comment.service

import boki.board.comment.entity.Comment
import boki.board.comment.repository.CommentRepository
import boki.board.comment.service.request.CommentCreateRequest
import boki.board.comment.service.response.CommentDetailResponse
import boki.board.common.snowflake.Snowflake
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Predicate

@Service
class CommentService(
    private val commentRepository: CommentRepository,
) {
    private val snowflake = Snowflake()

    @Transactional
    fun create(request: CommentCreateRequest): CommentDetailResponse {
        val parent = findParent(request)
        val comment = commentRepository.save(
            Comment.of(
                commentId = snowflake.nextId(),
                content = request.content,
                parentCommentId = parent?.commentId,
                articleId = request.articleId,
                writerId = request.writerId,
            )
        )
        return CommentDetailResponse.from(comment)
    }

    private fun findParent(request: CommentCreateRequest): Comment? {
        val parentCommentId = request.parentCommentId ?: return null
        return commentRepository.findByIdOrNull(parentCommentId)
            ?.takeIf { !it.deleted }
            ?.takeIf { it.isRoot }
            ?: throw RuntimeException("Comment not found with id: $parentCommentId")
    }

    private fun findParent2(request: CommentCreateRequest): Comment? {
        val parentCommentId = request.parentCommentId
        if (parentCommentId == null) {
            return null
        } else {
            return commentRepository.findById(parentCommentId)
                .filter(Predicate.not(Comment::deleted))
                .filter(Comment::isRoot)
                .orElseThrow() // NoSuchElementException
        }
    }

    fun read(commentId: Long): CommentDetailResponse {
        return CommentDetailResponse.from(
            readCommentById(commentId)
        )
    }

    @Transactional
    fun delete(commentId: Long) {
        readCommentById(commentId)
            .takeIf { !it.deleted }
            ?.let { comment ->
                if (hasChildren(comment)) {
                    comment.delete()
                } else {
                    delete(comment)
                }
            }
    }

    private fun hasChildren(comment: Comment): Boolean {
        return commentRepository.countBy(comment.articleId, comment.commentId, 2L) == 2L
    }

    private fun delete(comment: Comment) {
        commentRepository.delete(comment)
        if (!comment.isRoot) {
            commentRepository.findById(comment.parentCommentId)
                .filter(Comment::deleted)
                .filter(Predicate.not(this::hasChildren))
                .ifPresent(this::delete)
        }
    }

    private fun readCommentById(commentId: Long): Comment {
        return commentRepository.findByIdOrNull(commentId)
            ?: throw RuntimeException("Comment not found with id: $commentId")
    }

}