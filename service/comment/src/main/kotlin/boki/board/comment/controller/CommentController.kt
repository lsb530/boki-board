package boki.board.comment.controller

import boki.board.comment.service.CommentService
import boki.board.comment.service.request.CommentCreateRequest
import boki.board.comment.service.response.CommentDetailResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RequestMapping(value = ["/v1/comments"])
@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @GetMapping("/{commentId}")
    fun read(
        @PathVariable commentId: Long
    ): ResponseEntity<CommentDetailResponse> {
        return ResponseEntity.ok(commentService.read(commentId))
    }

    @PostMapping
    fun create(
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentDetailResponse> {
        return ResponseEntity.ok(commentService.create(request))
    }

    @DeleteMapping("/{commentId}")
    fun delete(
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {
        commentService.delete(commentId)
        return ResponseEntity.noContent().build()
    }
}