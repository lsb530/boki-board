-- create database article;
CREATE TABLE article
(
    article_id  bigint        NOT NULL PRIMARY KEY,
    title       varchar(100)  NOT NULL,
    content     varchar(3000) NOT NULL,
    board_id    bigint        NOT NULL,
    writer_id   bigint        NOT NULL,
    created_at  datetime      NOT NULL,
    modified_at datetime      NOT NULL
);