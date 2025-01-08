package boki.board.article.repository

import boki.board.article.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {

    @Query(
        value = "SELECT article.article_id, article.title, article.content, article.board_id, article.writer_id, article.created_at, article.modified_at " +
                "FROM (" +
                "   SELECT article_id FROM article" +
                "   WHERE board_id = :boardId " +
                "   ORDER BY article_id DESC " +
                "   LIMIT :limit OFFSET :offset " +
                ") AS t LEFT JOIN article ON t.article_id = article.article_id",
        nativeQuery = true
    )
    fun findAll(
        @Param("boardId") boardId: Long,
        @Param("offset") offset: Long,
        @Param("limit") limit: Long,
    ): List<Article>

    @Query(
        value = "SELECT COUNT(*) FROM (" +
                "   SELECT article_id FROM article" +
                "   WHERE board_id = :boardId" +
                "   LIMIT :limit" +
                ") AS t",
        nativeQuery = true
    )
    fun count(
        @Param("boardId") boardId: Long,
        @Param("limit") limit: Long
    ): Long
}