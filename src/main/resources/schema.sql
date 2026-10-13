create database discodeit;

create user discodeit_user WITH PASSWORD 'discodeit1234';
GRANT CONNECT ON DATABASE discodeit TO discodeit_user;

GRANT CONNECT ON DATABASE discodeit TO discodeit_user;

GRANT USAGE ON SCHEMA public TO discodeit_user;

GRANT SELECT, INSERT, UPDATE, DELETE
    ON ALL TABLES IN SCHEMA public
    TO discodeit_user;

GRANT USAGE, SELECT
    ON ALL SEQUENCES IN SCHEMA public
    TO discodeit_user;

--위의 코드들 디비버에서 실행한 후 생성한 유저로 연결했습니다.

CREATE TABLE binary_contents(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    file_name varchar(255) NOT NULL ,
    size bigint NOT NULL ,
    content_type varchar(100) NOT NULL,
    bytes bytea NOT NULL
);

CREATE TABLE users(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    updated_at timestamptz,
    username varchar(50) NOT NULL UNIQUE ,
    email varchar(100) NOT NULL UNIQUE ,
    password varchar(60) NOT NULL ,
    profile_id UUID UNIQUE REFERENCES binary_contents(id) ON DELETE SET NULL
);

CREATE TABLE user_statuses(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    updated_at timestamptz,
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE NOT NULL ,
    last_active_at timestamptz NOT NULL
);



CREATE TABLE channels(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    updated_at timestamptz,
    name varchar(100),
    description varchar(500),
    type varchar(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    updated_at timestamptz,
    content text,
    channel_id UUID NOT NULL REFERENCES channels(id) ON DELETE CASCADE ,
    author_id UUID REFERENCES users(id) ON DELETE  SET NULL
);

CREATE TABLE message_attachments(
    message_id UUID REFERENCES  messages(id) ON  DELETE CASCADE ,
    attachment_id UUID REFERENCES binary_contents(id) ON DELETE CASCADE,
    PRIMARY KEY (message_id, attachment_id)
    --코드잇에서 제공된 ERD상 PK가 명시되지 않아 임의로 복합PK 넣었습니다.
);




CREATE TABLE read_statuses(
    id UUID PRIMARY KEY ,
    created_at timestamptz NOT NULL ,
    updated_at timestamptz,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE ,
    channel_id UUID NOT NULL  REFERENCES  channels(id) ON DELETE CASCADE ,
    last_read_at timestamptz NOT NULL,
    UNIQUE (user_id,channel_id)
)
