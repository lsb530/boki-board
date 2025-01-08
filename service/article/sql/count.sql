# 게시글 개수
# COUNT + LIMIT 사용 불가
# Covering Index join -> Count
# 사용자가 10,001~10,010 페이지에 있다고 가정
SELECT count(*)
FROM (SELECT article_id
      FROM article
      WHERE board_id = 1
      ORDER BY article_id DESC
      LIMIT 300301) AS t;
# 0.09초

EXPLAIN SELECT count(*)
        FROM (SELECT article_id
              FROM article
              WHERE board_id = 1
              ORDER BY article_id DESC
              LIMIT 300301) AS t;
