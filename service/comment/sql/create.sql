CREATE TABLE comment
(
    comment_id        bigint        NOT NULL PRIMARY KEY,
    content           varchar(3000) NOT NULL,
    article_id        bigint        NOT NULL,
    parent_comment_id bigint        NOT NULL,
    writer_id         bigint        NOT NULL,
    deleted           bool          NOT NULL,
    created_at        datetime      NOT NULL
);