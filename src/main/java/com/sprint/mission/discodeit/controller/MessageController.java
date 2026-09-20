package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> postMessage(
            @RequestParam("content") String content,
            @RequestParam("channel-id") UUID channelId,
            @RequestParam("author-id") UUID authorId,
            @RequestParam(value = "images", required = false) List<MultipartFile> files) {
        MessageCreateRequest request = new MessageCreateRequest(content, channelId, authorId);
        List<BinaryContentCreateRequest> binaryContents = null;
        if (files != null && !files.isEmpty()) {
            binaryContents = files.stream().map(BinaryContentCreateRequest::from).toList();
        }

        return ResponseEntity.ok().body(messageService.create(request, binaryContents));
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<Message> updateMessage(
            @RequestParam("message-id") UUID messageId,
            @RequestParam("content") String content) {
        MessageUpdateRequest request = new MessageUpdateRequest(messageId, content);

        return ResponseEntity.ok().body(messageService.update(request));
    }

    @RequestMapping(path = "/{message-id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable("message-id") UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/{channel-id}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessages(@PathVariable("channel-id") UUID channelId) {
        return ResponseEntity.ok().body(messageService.findAllByChannelId(channelId));
    }
}
