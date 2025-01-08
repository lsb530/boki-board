# 1번 게시판, 4번 페이지에서 30건의 데이터 조회
SELECT *
FROM article
WHERE board_id = 1
ORDER BY created_at DESC
LIMIT 30 OFFSET 90;
# 3~4초 수행

EXPLAIN
SELECT *
FROM article
WHERE board_id = 1
ORDER BY created_at DESC
LIMIT 30 OFFSET 90;

EXPLAIN ANALYZE
SELECT *
FROM article
WHERE board_id = 1
ORDER BY created_at DESC
LIMIT 30 OFFSET 90;


# 인덱스 생성 이후
# 멀티스레드 환경때문에 created_at은 동일컬럼이 생성될 수 있음
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 90;
# 0.2초 수행

EXPLAIN
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 90;

EXPLAIN ANALYZE
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 90;


# 50,000 페이지 조회
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 1499970;
# 4초 수행(뒷 페이지로 갈수록 검색이 느려짐)

EXPLAIN
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 1499970;

EXPLAIN ANALYZE
SELECT *
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 1499970;


SELECT *
FROM article
LIMIT 1;
SELECT *
FROM article
WHERE article_id = 135312078832545792;
EXPLAIN
SELECT *
FROM article
WHERE article_id = 135312078832545792;


# 커버링 인덱스
SELECT board_id, article_id
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 1499970;
# 0.2초 수행

EXPLAIN
SELECT board_id, article_id
FROM article
WHERE board_id = 1
ORDER BY article_id DESC
LIMIT 30 OFFSET 1499970;

# 커버링인덱스 사용 후 필요한 데이터 추출
SELECT *
FROM (SELECT article_id
      FROM article
      WHERE board_id = 1
      ORDER BY article_id DESC
      LIMIT 30 OFFSET 1499970) AS t
         LEFT JOIN article ON t.article_id = article.article_id;
# 탐색과정: 커버링인덱스 사용 -> 검색된 데이터 주소를 다시 Clustered Index에서 참조해서 데이터 추출

EXPLAIN
SELECT *
FROM (SELECT article_id
      FROM article
      WHERE board_id = 1
      ORDER BY article_id DESC
      LIMIT 30 OFFSET 1499970) AS t
         LEFT JOIN article ON t.article_id = article.article_id;


# 300,000번 페이지 조회
SELECT *
FROM (SELECT article_id
      FROM article
      WHERE board_id = 1
      ORDER BY article_id DESC
      LIMIT 30 OFFSET 8999970) AS t
         LEFT JOIN article ON t.article_id = article.article_id;
# 동일한 쿼리 플랜이 실행되지만, 뒤에 있는 데이터를 찾을수록 느려지는건 동일