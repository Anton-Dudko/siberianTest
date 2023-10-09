drop table if exists task;
create table task
(
    id                bigint not null primary key generated always as identity,
    title             varchar(256),
    description       varchar(10240),
    creation_date     timestamp(6),
    modification_date timestamp(6),
    is_completed      boolean not null
);