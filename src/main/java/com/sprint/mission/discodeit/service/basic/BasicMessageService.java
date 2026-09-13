package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messages;
    private final BinaryContentRepository binaries;

    public Message create(MessageCreateRequest request, List<BinaryContentCreateRequest> attachments) {
        List<UUID> attachmentIds = new ArrayList<>();
        if (attachments != null) {
            for (BinaryContentCreateRequest attachment : attachments) {
                BinaryContent file = new BinaryContent(
                        attachment.getFileName(), attachment.getContentType(), attachment.getBytes());
                binaries.save(file);
                attachmentIds.add(file.getId());
            }
        }
        Message message = new Message(request.getChannelId(), request.getAuthorId(), request.getContent(), attachmentIds);
        return messages.save(message);
    }

    public List<Message> findAllByChannelId(UUID channelId) {
        return messages.findAllByChannelId(channelId);
    }

    public Message update(MessageUpdateRequest request) {
        Message message = messages.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
        message.update(request.getContent());
        return messages.save(message);
    }

    public void delete(UUID id) {
        Message message = messages.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
        for (UUID attachmentId : message.getAttachmentIds()) {
            binaries.deleteById(attachmentId);
        }
        messages.deleteById(id);
    }
}
