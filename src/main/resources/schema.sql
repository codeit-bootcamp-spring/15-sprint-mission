-- 1. 기존 테이블 삭제 (외래키 의존성 역순)
DROP TABLE IF EXISTS message_attachments CASCADE;
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;

-- 2. 바이너리 컨텐츠 (BinaryContent: 메타데이터만 관리)
CREATE TABLE binary_contents (
                                 id UUID PRIMARY KEY,
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                 updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                 file_name VARCHAR(255) NOT NULL,
                                 size BIGINT NOT NULL,
                                 content_type VARCHAR(100) NOT NULL
);

-- 3. 유저 (User)
-- 프로필 바이너리 삭제 시 NULL 처리 (ON DELETE SET NULL)
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       profile_id UUID,
                       CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
                           REFERENCES binary_contents(id) ON DELETE SET NULL
);

-- 4. 유저 상태 (UserStatus)
-- 유저 삭제 시 상태 함께 삭제 (ON DELETE CASCADE)
CREATE TABLE user_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               user_id UUID NOT NULL UNIQUE,
                               last_active_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               CONSTRAINT fk_user_statuses_user FOREIGN KEY (user_id)
                                   REFERENCES users(id) ON DELETE CASCADE
);

-- 5. 채널 (Channel)
CREATE TABLE channels (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          type VARCHAR(20) NOT NULL,
                          description VARCHAR(255)
);

-- 6. 메시지 (Message)
-- 채널/작성자 삭제 시 메시지 연쇄 삭제 (ON DELETE CASCADE)
CREATE TABLE messages (
                          id UUID PRIMARY KEY,
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          content TEXT NOT NULL,
                          channel_id UUID NOT NULL,
                          author_id UUID NOT NULL,
                          CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id)
                              REFERENCES channels(id) ON DELETE CASCADE,
                          CONSTRAINT fk_messages_author FOREIGN KEY (author_id)
                              REFERENCES users(id) ON DELETE CASCADE
);

-- 7. 메시지 첨부파일 매핑 테이블 (Message Attachments)
-- 다대다(N:M) 또는 일대다 연관관계 매핑 테이블
CREATE TABLE message_attachments (
                                     message_id UUID NOT NULL,
                                     binary_content_id UUID NOT NULL,
                                     PRIMARY KEY (message_id, binary_content_id),
                                     CONSTRAINT fk_attachments_message FOREIGN KEY (message_id)
                                         REFERENCES messages(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_attachments_content FOREIGN KEY (binary_content_id)
                                         REFERENCES binary_contents(id) ON DELETE CASCADE
);

-- 8. 읽음 상태 (ReadStatus)
-- 한 유저는 한 채널당 하나의 읽음 상태만 보유 (user_id, channel_id 복합 UNIQUE)
CREATE TABLE read_statuses (
                               id UUID PRIMARY KEY,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               user_id UUID NOT NULL,
                               channel_id UUID NOT NULL,
                               last_read_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               CONSTRAINT uk_read_status_user_channel UNIQUE (user_id, channel_id),
                               CONSTRAINT fk_read_statuses_user FOREIGN KEY (user_id)
                                   REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_read_statuses_channel FOREIGN KEY (channel_id)
                                   REFERENCES channels(id) ON DELETE CASCADE
);