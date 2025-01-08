package boki.board.article.api

import boki.board.article.service.request.ArticleCreateRequest
import boki.board.article.service.request.ArticleUpdateRequest
import boki.board.article.service.response.ArticleDetailResponse
import boki.board.article.service.response.ArticlePageResponse
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
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

    @Test
    fun readAllTest() {
        val page1 = Triple(1L, 1L, 30L)
        val response1 = readAll(
            boardId = page1.first,
            page = page1.second,
            pageSize = page1.third
        )
        assertTrue { response1.statusCode.is2xxSuccessful }
        assertEquals(300 + 1, response1.body!!.count) // 301
        // response1.body?.let {
        //     for (article in it.articles)
        //         println(article.articleId)
        // }

        val page50000 = Triple(1L, 50000L, 30L)
        val response2 = readAll(
            boardId = page50000.first,
            page = page50000.second,
            pageSize = page50000.third
        )
        assertTrue { response2.statusCode.is2xxSuccessful }
        assertEquals(1_500_000 + 1, response2.body!!.count) // 301
    }

    @Test
    fun readAllInfiniteScrollTest() {
        val response1 = readAllInfiniteScroll(
            boardId = 1L,
            pageSize = 5L
        )
        println("first page")
        for (article in response1.body!!) {
            println(article.articleId)
        }

        val lastArticleId = response1.body!!.last().articleId
        val response2 = readAllInfiniteScroll(
            boardId = 1L,
            pageSize = 5L,
            lastArticleId = lastArticleId
        )
        println("second page")
        for (article in response2.body!!) {
            println(article.articleId)
        }
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

    private fun readAll(
        boardId: Long,
        page: Long,
        pageSize: Long,
    ): ResponseEntity<ArticlePageResponse> {
        return restClient.get()
            .uri("/v1/articles?boardId=$boardId&page=$page&pageSize=$pageSize")
            .retrieve()
            .toEntity(ArticlePageResponse::class.java)
    }

    private fun readAllInfiniteScroll(
        boardId: Long,
        pageSize: Long,
        lastArticleId: Long? = null
    ): ResponseEntity<List<ArticleDetailResponse>> {
        val url = if (lastArticleId == null) {
            "/v1/articles/infinite-scroll?boardId=$boardId&pageSize=$pageSize"
        } else {
            "/v1/articles/infinite-scroll?boardId=$boardId&pageSize=$pageSize&lastArticleId=$lastArticleId"
        }
        return restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<List<ArticleDetailResponse>>() {})
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