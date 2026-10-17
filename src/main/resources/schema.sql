create table users
(
    id         uuid primary key,
    created_at timestamptz  not null,
    updated_at timestamptz,
    username   varchar(50)  not null unique,
    email      varchar(100) not null unique,
    password   varchar(60)  not null,
    profile_id uuid         REFERENCES binary_contents (id) on delete set null unique
);

create table binary_contents
(
    id           uuid primary key,
    created_at   timestamptz  not null,
    file_name    varchar(255) not null,
    size         bigint       not null,
    content_type varchar(100) not null,
    bytes        bytea        not null
);

create table user_statuses
(
    id             uuid primary key,
    created_at     timestamptz not null,
    updated_at     timestamptz,
    user_id        uuid REFERENCES users (id) on delete cascade unique,
    last_active_at timestamptz not null
);

create type type as enum ('PUBLIC','PRIVATE');

create table channels
(
    id          uuid primary key,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        type        not null
);

create table messages
(
    id         uuid primary key,
    created_at timestamptz                                     not null,
    updated_at timestamptz,
    contents   text,
    channel_id uuid REFERENCES channels (id) on delete cascade not null,
    author_id  uuid                                            REFERENCES users (id) on delete set null
);

create table read_statuses
(
    id             uuid primary key,
    created_at     timestamptz                                     not null,
    updated_at     timestamptz,
    user_id        uuid REFERENCES users (id) on delete cascade    not null,
    channel_id     uuid REFERENCES channels (id) on delete cascade not null,
    last_active_at timestamptz                                     not null
);

create table message_attachments
(
    message_id    uuid REFERENCES messages (id) on delete cascade        not null,
    attachment_id uuid REFERENCES binary_contents (id) on delete cascade not null
);

