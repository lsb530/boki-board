package boki.board.comment.service.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CommentCreateRequest(
    @field:NotNull(message = "게시글 아이디는 비어있을 수 없습니다")
    val articleId: Long,
    @field:NotBlank(message = "내용은 비어있을 수 없습니다")
    val content: String,
    val parentCommentId: Long?,
    @field:NotNull(message = "작성자 아이디는 비어있을 수 없습니다")
    @field:Positive(message = "작성자 아이디는 음수일 수 없습니다")
    val writerId: Long,
)