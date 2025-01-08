package boki.board.article.service

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class PageLimitCalculatorTest {

    @ParameterizedTest
    @CsvSource(
        "1, 30, 10, 301",
        "7, 30, 10, 301",
        "10, 30, 10, 301",
        "11, 30, 10, 601",
        "12, 30, 10, 601"
    )
    fun calculatePageLimitTest(page: Long, pageSize: Long, movablePageCount: Long, expected: Long) {
        val result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount)
        assertEquals(expected, result)
    }

}