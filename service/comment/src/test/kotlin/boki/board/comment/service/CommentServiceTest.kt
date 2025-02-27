package boki.board.comment.service

import boki.board.comment.entity.Comment
import boki.board.comment.repository.CommentRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class CommentServiceTest {

    @InjectMocks
    lateinit var commentService: CommentService

    @Mock
    lateinit var commentRepository: CommentRepository

    @Test
    @DisplayName("삭제할 댓글이 자식 댓글을 갖고 있다면, 삭제 표시만 한다.")
    fun deleteShouldMarkDeletedIfHasChildren() {
        // given
        val articleId = 1L
        val commentId = 2L

        val comment = createMockComment(articleId, commentId)

        // BDDMockito.given(commentRepository.findByIdOrNull(commentId)) // 내부적으로 findById를 사용
        //     .willReturn(comment)
        given(commentRepository.findById(commentId))
            .willReturn(Optional.of(comment))
        given(commentRepository.countBy(articleId = articleId, parentCommentId = commentId, 2))
            .willReturn(2L)

        // when
        commentService.delete(commentId)

        // then
        verify(comment).delete()
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제되지 않은 부모면 하위 댓글만 삭제한다.")
    fun deleteShouldDeleteChildOnlyIfNotDeletedParent() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 1L

        val comment = createMockComment(articleId, commentId, parentCommentId)
        given(comment.isRoot).willReturn(false)

        val parentComment = mock(Comment::class.java)
        given(parentComment.deleted).willReturn(false)

        given(commentRepository.findById(commentId))
            .willReturn(Optional.of(comment))
        given(commentRepository.countBy(articleId, commentId, 2))
            .willReturn(1L) // hasChildren: false
        given(commentRepository.findById(parentCommentId))
            .willReturn(Optional.of(parentComment))

        // when
        commentService.delete(commentId)

        // then
        verify(commentRepository).delete(comment)
        verify(commentRepository, never()).delete(parentComment)
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제된 부모라면 재귀적으로 모두 삭제한다.")
    fun deleteShouldDeleteAllRecursivelyIfDeletedParent() {
        // given
        val articleId = 1L
        val commentId = 2L
        val parentCommentId = 1L

        val comment = createMockComment(articleId, commentId, parentCommentId)
        given(comment.isRoot).willReturn(false)

        val parentComment = createMockComment(articleId, parentCommentId)
        given(parentComment.isRoot).willReturn(true)
        given(parentComment.deleted).willReturn(true)

        given(commentRepository.findById(commentId))
            .willReturn(Optional.of(comment))
        given(commentRepository.countBy(articleId, commentId, 2))
            .willReturn(1L) // hasChildren: false

        given(commentRepository.findById(parentCommentId))
            .willReturn(Optional.of(parentComment))
        given(commentRepository.countBy(articleId, parentCommentId, 2))
            .willReturn(1L)

        // when
        commentService.delete(commentId)

        // then
        verify(commentRepository).delete(comment)
        verify(commentRepository).delete(parentComment)
    }

    private fun createMockComment(
        articleId: Long,
        commentId: Long,
        parentCommentId: Long? = null,
    ): Comment {
        val comment = mock(Comment::class.java)
        given(comment.articleId).willReturn(articleId)
        given(comment.commentId).willReturn(commentId)
        parentCommentId?.let {
            given(comment.parentCommentId).willReturn(parentCommentId)
        }
        return comment
    }
}