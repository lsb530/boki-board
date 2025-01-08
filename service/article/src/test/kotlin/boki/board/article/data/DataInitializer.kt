package boki.board.article.data

import boki.board.article.entity.Article
import boki.board.common.snowflake.Snowflake
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

const val BULK_INSERT_SIZE = 2_000 // per
const val EXECUTE_COUNT = 6_000 // total

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DataInitializer(
    @PersistenceContext
    val entityManager: EntityManager,
    @Autowired
    val transactionTemplate: TransactionTemplate
) {
    private val snowflake = Snowflake()
    private val latch = CountDownLatch(EXECUTE_COUNT)

    @Test
    fun initialize() {
        val executorService = Executors.newFixedThreadPool(10)
        for (i in 0 until EXECUTE_COUNT) {
            executorService.submit {
                insert()
                latch.countDown()
                if ((latch.count % 1000).toInt() == 0) {
                    println("latch.count=${latch.count}")
                }
            }
        }
        latch.await()
        executorService.shutdown()
    }

    fun insert() {
        transactionTemplate.executeWithoutResult {
            for (i in 0 until BULK_INSERT_SIZE) {
                val article = Article.of(
                    articleId = snowflake.nextId(),
                    title = "title$i",
                    content = "content$i",
                    boardId = 1L,
                    writerId = 1L
                )
                entityManager.persist(article)
            }
        }
    }
}