CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        bytea        NOT NULL
);

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ         NOT NULL,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID UNIQUE,

    FOREIGN KEY (profile_id)
        REFERENCES binary_contents (id)
        ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    last_active_at TIMESTAMPTZ NOT NULL,
    user_id        UUID UNIQUE NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL
        CHECK ( type IN ('PUBLIC', 'PRIVATE') )
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ,
    user_id      UUID        NOT NULL,
    channel_id   UUID        NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,

    UNIQUE (user_id, channel_id)
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    text,
    channel_id UUID        NOT NULL,
    author_id  UUID,

    FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,
    FOREIGN KEY (author_id)
        REFERENCES users (id)
        ON DELETE SET NULL
);

CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,

    FOREIGN KEY (message_id)
        REFERENCES messages (id)
        ON DELETE CASCADE,
    FOREIGN KEY (attachment_id)
        REFERENCES binary_contents (id)
        ON DELETE CASCADE
);