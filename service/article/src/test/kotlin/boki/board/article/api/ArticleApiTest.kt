package boki.board.article.api

import boki.board.article.service.request.ArticleCreateRequest
import boki.board.article.service.request.ArticleUpdateRequest
import boki.board.article.service.response.ArticleDetailResponse
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClient
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArticleApiTest {

    private val restClient: RestClient = RestClient.create(
        "http://localhost:9000",
    )

    @Test
    fun createTest() {
        val response = create(
            ArticleCreateRequest(
                title = "hi",
                content = "my content",
                writerId = 1L,
                boardId = 1L
            )
        )
        assertTrue { response.statusCode.is2xxSuccessful }
            .also {
                println(response.body)
            }
    }

    @Test
    fun readTest() {
        val response = read(135254241543389184)
        assertTrue { response.statusCode.is2xxSuccessful }
            .also {
                println(response.body)
            }
    }

    @Test
    fun updateTest() {
        val request = ArticleUpdateRequest(
            title = "bye",
            content = "updated content",
        )
        val response = update(
            articleId = 135254241543389184,
            request = request
        )
        assertTrue { response.statusCode.is2xxSuccessful }
        assertEquals(request.title, response.body!!.title)
        assertEquals(request.content, response.body!!.content)
        println(response.body)
    }

    @Test
    fun deleteTest() {
        val articleId = 135254241543389184

        val response = delete(articleId)
        read(articleId)

        assertTrue { response.statusCode.is2xxSuccessful }
    }

    private fun create(
        request: ArticleCreateRequest
    ): ResponseEntity<ArticleDetailResponse> {
        return restClient.post()
            .uri("/v1/articles")
            .body(request)
            .retrieve()
            .toEntity(ArticleDetailResponse::class.java)
    }

    private fun read(
        articleId: Long
    ): ResponseEntity<ArticleDetailResponse> {
        return restClient.get()
            .uri("/v1/articles/$articleId")
            .retrieve()
            .toEntity(ArticleDetailResponse::class.java)
    }

    private fun update(
        articleId: Long,
        request: ArticleUpdateRequest
    ): ResponseEntity<ArticleDetailResponse> {
        return restClient.patch()
            .uri("/v1/articles/$articleId")
            .body(request)
            .retrieve()
            .toEntity(ArticleDetailResponse::class.java)
    }

    private fun delete(
        articleId: Long
    ): ResponseEntity<Void> {
        return restClient.delete()
            .uri("/v1/articles/$articleId")
            .retrieve()
            .toBodilessEntity()
    }
}