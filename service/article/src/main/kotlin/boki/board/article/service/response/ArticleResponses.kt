package boki.board.article.service.response

import boki.board.article.entity.Article
import java.time.LocalDateTime

data class ArticleDetailResponse(
    val articleId: Long,
    val title: String,
    val content: String,
    val boardId: Long,
    val writerId: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun from(article: Article): ArticleDetailResponse {
            return ArticleDetailResponse(
                articleId = article.articleId!!,
                title = article.title,
                content = article.content,
                boardId = article.boardId,
                writerId = article.writerId,
                createdAt = article.createdAt,
                modifiedAt = article.modifiedAt,
            )
        }
    }
}

data class ArticlePageResponse(
    val articles: List<ArticleDetailResponse>,
    val count: Long,
) {
    companion object {
        fun of(articles: List<ArticleDetailResponse>, count: Long): ArticlePageResponse {
            return ArticlePageResponse(
                articles = articles,
                count = count
            )
        }
    }
}