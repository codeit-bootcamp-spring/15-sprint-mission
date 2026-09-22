package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final FileMessageRepository messageRepository;

    private final UserService userService;

    private final ChannelService channelService;

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {

        // 작성자가 존재하는지 확인
        userService.findById(authorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 사용자입니다.")
                );

        // 채널이 존재하는지 확인
        channelService.findById(channelId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 채널입니다.")
                );

        Message message = new Message(authorId, channelId, content);

        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> update(UUID id, String content) {

        Optional<Message> optionalMessage =
                messageRepository.findById(id);

        if (optionalMessage.isEmpty()) {
            return Optional.empty();
        }

        Message message = optionalMessage.get();
        message.setContent(content);

        messageRepository.save(message);

        return Optional.of(message);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }

    @Override
    public void like(UUID messageId, UUID userId) {

        Optional<Message> optionalMessage =
                messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return;
        }

        Message message = optionalMessage.get();

        message.getLikeUserIds().add(userId);

        messageRepository.save(message);
    }

    @Override
    public void unlike(UUID messageId, UUID userId) {

        Optional<Message> optionalMessage =
                messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return;
        }

        Message message = optionalMessage.get();

        message.getLikeUserIds().remove(userId);

        messageRepository.save(message);
    }

    @Override
    public Set<UUID> getLikeUserIds(UUID messageId) {

        Optional<Message> optionalMessage =
                messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return Set.of();
        }

        return optionalMessage.get().getLikeUserIds();
    }
}