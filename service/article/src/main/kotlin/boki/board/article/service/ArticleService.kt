package boki.board.article.service

import boki.board.article.entity.Article
import boki.board.article.repository.ArticleRepository
import boki.board.article.service.request.ArticleCreateRequest
import boki.board.article.service.request.ArticleUpdateRequest
import boki.board.article.service.response.ArticleDetailResponse
import boki.board.common.snowflake.Snowflake
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    private val snowflake = Snowflake()

    @Transactional
    fun create(request: ArticleCreateRequest): ArticleDetailResponse {
        val article = articleRepository.save(
            Article.of(
                articleId = snowflake.nextId(),
                title = request.title,
                content = request.content,
                boardId = request.boardId,
                writerId = request.writerId,
            )
        )
        return ArticleDetailResponse.from(article)
    }

    fun read(articleId: Long): ArticleDetailResponse {
        val article = readArticleById(articleId)
        return ArticleDetailResponse.from(article)
    }

    @Transactional
    fun update(articleId: Long, request: ArticleUpdateRequest): ArticleDetailResponse {
        val article = readArticleById(articleId)
        article.update(
            title = request.title,
            content = request.content,
        )
        return ArticleDetailResponse.from(article)
    }

    @Transactional
    fun delete(articleId: Long) {
        val article = readArticleById(articleId)
        articleRepository.delete(article)
    }

    private fun readArticleById(articleId: Long): Article {
        return articleRepository.findByIdOrNull(articleId)
            ?: throw RuntimeException("Article not found with id: $articleId")
    }

}