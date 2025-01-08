package boki.board.article.service

object PageLimitCalculator {
    fun calculatePageLimit(
        page: Long,
        pageSize: Long,
        movablePageCount: Long
    ): Long {
        return ((page - 1) / movablePageCount + 1) * pageSize * movablePageCount + 1
    }
}