package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final BinaryContentService binaryContentService;

    // 메시지 전송
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> create(
            @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<UUID> attachmentIds = resolveAttachmentIds(attachments);

        MessageCreateRequest requestWithAttachments = new MessageCreateRequest(
                messageCreateRequest.contents(),
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                attachmentIds
        );

        MessageResponse response = messageService.create(requestWithAttachments);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageResponse> update(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {
        return ResponseEntity.ok(messageService.update(messageId, request));
    }

    // 메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널의 메시지 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> readAllByChannelId(@RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.readAllByChannelId(channelId));
    }

    private List<UUID> resolveAttachmentIds(List<MultipartFile> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return null;
        }
        return attachments.stream()
                .filter(file -> !file.isEmpty())
                .map(this::toBinaryContentCreateRequest)
                .map(binaryContentService::create)
                .map(BinaryContent::getId)
                .toList();
    }

    private BinaryContentCreateRequest toBinaryContentCreateRequest(MultipartFile file) {
        try {
            return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new UncheckedIOException("첨부파일 처리 중 오류 발생", e);
        }
    }
}