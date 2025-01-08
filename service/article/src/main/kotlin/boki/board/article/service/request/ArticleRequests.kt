package boki.board.article.service.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ArticleCreateRequest(
    @field:NotBlank(message = "제목은 비어있을 수 없습니다")
    val title: String,
    @field:NotBlank(message = "내용은 비어있을 수 없습니다")
    val content: String,
    @field:NotNull(message = "작성자 아이디는 비어있을 수 없습니다")
    @field:Positive(message = "작성자 아이디는 음수일 수 없습니다")
    val writerId: Long,
    @field:NotNull(message = "게시판 아이디는 비어있을 수 없습니다")
    @field:Positive(message = "게시판 아이디는 음수일 수 없습니다")
    val boardId: Long,
)

data class ArticleUpdateRequest(
    @field:NotBlank(message = "제목은 비어있을 수 없습니다")
    val title: String,
    @field:NotBlank(message = "내용은 비어있을 수 없습니다")
    val content: String,
)