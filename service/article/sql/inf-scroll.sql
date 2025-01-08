# 세컨더리 인덱스 사용: 첫 번째 페이지
SELECT * FROM article
    WHERE board_id = 1
    ORDER BY article_id DESC
    LIMIT 30;

EXPLAIN SELECT * FROM article
        WHERE board_id = 1
        ORDER BY article_id DESC
        LIMIT 30;

# 세컨더리 인덱스 사용: 두 번째 페이지
SELECT * FROM article
WHERE board_id = 1 AND article_id < 135315633270305639
ORDER BY article_id DESC
LIMIT 30;

# 끝 페이지 조회
SELECT * FROM article
WHERE board_id = 1
ORDER BY article_id ASC
LIMIT 1 OFFSET 30;

# 마지막 페이지
SELECT * FROM article
WHERE board_id = 1 AND article_id < 135312078878683156
ORDER BY article_id DESC
LIMIT 30;