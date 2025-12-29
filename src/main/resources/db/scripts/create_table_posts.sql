create table posts
(
    id            serial primary key,
    link          text unique not null,
    creation_time timestamp   not null
);