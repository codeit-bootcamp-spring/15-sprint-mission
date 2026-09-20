package com.sprint.mission.discodeit.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/messages")
public class MessageController {
    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 생성
    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request, new ArrayList<>());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.from(message));
    }

    // 수정
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessage(
            @PathVariable UUID messageId,
            @Valid @RequestBody MessageUpdateRequest request) {
        Message message = messageService.update(messageId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(MessageResponse.from(message));
    }

    // 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널 메시지 조회
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByChannelId(
            @PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
