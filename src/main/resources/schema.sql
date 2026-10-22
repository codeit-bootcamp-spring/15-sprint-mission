
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       profile_id UUID
);


CREATE TABLE binary_contents (
                                 id UUID PRIMARY KEY,
                                 created_at TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP,
                                 file_name VARCHAR(255) NOT NULL,
                                 content_type VARCHAR(100),
                                 size BIGINT NOT NULL
);


ALTER TABLE users
    ADD CONSTRAINT fk_users_profile
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents(id);



CREATE TABLE channels (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          name VARCHAR(100),
                          description TEXT,
                          type VARCHAR(20) NOT NULL
);


CREATE TABLE channel_participants (
                                      channel_id UUID NOT NULL,
                                      user_id UUID NOT NULL,

                                      PRIMARY KEY (channel_id, user_id),

                                      CONSTRAINT fk_participants_channel
                                          FOREIGN KEY (channel_id)
                                              REFERENCES channels(id),

                                      CONSTRAINT fk_participants_user
                                          FOREIGN KEY (user_id)
                                              REFERENCES users(id)
);



CREATE TABLE messages (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          content TEXT,
                          channel_id UUID NOT NULL,
                          author_id UUID NOT NULL,

                          CONSTRAINT fk_messages_channel
                              FOREIGN KEY (channel_id)
                                  REFERENCES channels(id),

                          CONSTRAINT fk_messages_author
                              FOREIGN KEY (author_id)
                                  REFERENCES users(id)
);


CREATE TABLE message_attachments (
                                     message_id UUID NOT NULL,
                                     binary_content_id UUID NOT NULL,

                                     PRIMARY KEY (message_id, binary_content_id),

                                     CONSTRAINT fk_attachments_message
                                         FOREIGN KEY (message_id)
                                             REFERENCES messages(id),

                                     CONSTRAINT fk_attachments_binary_content
                                         FOREIGN KEY (binary_content_id)
                                             REFERENCES binary_contents(id)
);



CREATE TABLE read_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP,
                               user_id UUID NOT NULL,
                               channel_id UUID NOT NULL,
                               last_read_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_read_statuses_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id),

                               CONSTRAINT fk_read_statuses_channel
                                   FOREIGN KEY (channel_id)
                                       REFERENCES channels(id),

                               CONSTRAINT uq_read_statuses_user_channel
                                   UNIQUE (user_id, channel_id)
);


CREATE TABLE user_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP,
                               user_id UUID NOT NULL UNIQUE,
                               last_active_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_user_statuses_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
);


