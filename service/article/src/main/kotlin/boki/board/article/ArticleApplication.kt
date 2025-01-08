package boki.board.article

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = ["boki.board"])
class ArticleApplication

fun main(args: Array<String>) {
    runApplication<ArticleApplication>(*args)
}