package boki.board.article.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "article")
@Entity
class Article protected constructor (
    @Id
    val articleId: Long? = null,
    var title: String,
    var content: String,
    val boardId: Long, // shared key
    val writerId: Long,
    val createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime,
) {

    companion object {
        fun of(
            articleId: Long,
            title: String,
            content: String,
            boardId: Long,
            writerId: Long,
        ): Article {
            return Article(
                articleId = articleId,
                title = title,
                content = content,
                boardId = boardId,
                writerId = writerId,
                createdAt = LocalDateTime.now(),
                modifiedAt = LocalDateTime.now(),
            )
        }
    }

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
        this.modifiedAt = LocalDateTime.now()
    }
}