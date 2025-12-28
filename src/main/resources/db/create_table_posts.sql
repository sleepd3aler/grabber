create table posts
(
    id            serial primary key,
    text          description,
    text          link unique,
    creation_time timestamp
);