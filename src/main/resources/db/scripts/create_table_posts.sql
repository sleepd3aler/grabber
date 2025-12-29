create table posts
(
    id            serial primary key,
    description   text        not null,
    link          text unique not null,
    creation_time timestamp   not null
);