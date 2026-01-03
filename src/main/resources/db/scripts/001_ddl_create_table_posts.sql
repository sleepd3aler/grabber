create table if not exists posts
(
    id            serial primary key,
    title        text,
    link          text unique not null,
    description   text,
    creation_time timestamp   not null
);

drop  table posts;


