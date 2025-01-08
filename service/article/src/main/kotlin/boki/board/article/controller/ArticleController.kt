package boki.board.article.controller

import boki.board.article.service.ArticleService
import boki.board.article.service.request.ArticleCreateRequest
import boki.board.article.service.request.ArticleUpdateRequest
import boki.board.article.service.response.ArticleDetailResponse
import boki.board.article.service.response.ArticlePageResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@Validated
@RequestMapping(value = ["/v1/articles"])
@RestController
class ArticleController(
    private val articleService: ArticleService,
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: ArticleCreateRequest
    ): ResponseEntity<ArticleDetailResponse> {
        val response = articleService.create(request)
        return ResponseEntity.created(
            URI.create("/articles/${response.articleId}")
        ).body(response)
    }

    @GetMapping("/{articleId}")
    fun read(
        @PathVariable @Positive articleId: Long
    ): ResponseEntity<ArticleDetailResponse> {
        return ResponseEntity.ok(articleService.read(articleId))
    }

    @GetMapping
    fun readAll(
        @RequestParam("boardId") boardId: Long,
        @RequestParam("page") page: Long,
        @RequestParam("pageSize") pageSize: Long,
    ): ResponseEntity<ArticlePageResponse> {
        return ResponseEntity.ok(
            articleService.readAll(
                boardId = boardId,
                page = page,
                pageSize = pageSize
            )
        )
    }

    @PatchMapping("/{articleId}")
    fun update(
        @PathVariable articleId: Long,
        @RequestBody request: ArticleUpdateRequest
    ): ResponseEntity<ArticleDetailResponse> {
        return ResponseEntity.ok(articleService.update(articleId, request))
    }

    @DeleteMapping("/{articleId}")
    fun delete(
        @PathVariable articleId: Long
    ): ResponseEntity<Void> {
        articleService.delete(articleId)
        return ResponseEntity.noContent().build()
    }

}