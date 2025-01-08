package boki.board.article.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ArticleRepositoryTest(
    @Autowired
    val articleRepository: ArticleRepository
) {
    @Test
    fun findAllTest() {
        val articles = articleRepository.findAll(
            boardId = 1L,
            offset = 1499970L,
            limit = 30L
        )
        println("articles.size = ${articles.size}")
        for (article in articles) {
            println(article)
        }
    }

    @Test
    fun countTest() {
        val count = articleRepository.count(
            boardId = 1L,
            limit = 10_000L
        )
        println("count = $count")
    }

    @Test
    fun findInfiniteScrollTest() {
        val articles = articleRepository.findAllInfiniteScroll(1L, 30L)
        for (article in articles) {
            println("articleId = ${article.articleId}")
        }

        val lastArticleId = articles.last().articleId!!
        val articles2 = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId)
        for (article in articles2) {
            println("articleId = ${article.articleId}")
        }
    }
}