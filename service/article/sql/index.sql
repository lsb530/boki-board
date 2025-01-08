-- 세컨더리(보조) 인덱스
CREATE INDEX idx_board_id_article_id
    ON article (board_id ASC, article_id DESC);