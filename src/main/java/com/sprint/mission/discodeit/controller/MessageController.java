package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import org.springframework.web.multipart.MultipartFile;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "multipart/form-data"
    )
    public ResponseEntity<MessageDto> create(
        @RequestPart("messageCreateRequest") MessageCreateRequest request,
        @RequestPart(value = "attachments", required = false)
        List<MultipartFile> attachments
    ) throws Exception {

        List<BinaryContentCreateRequest> attachmentRequests =
            attachments == null
                ? List.of()
                : attachments.stream()
                    .map(file -> {
                        try {
                            return new BinaryContentCreateRequest(
                                file.getOriginalFilename(),
                                file.getContentType(),
                                file.getBytes()
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

        MessageCreateRequest messageCreateRequest =
            new MessageCreateRequest(
                request.content(),
                request.channelId(),
                request.authorId(),
                attachmentRequests
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.create(messageCreateRequest));
    }
    @RequestMapping(
        path = "/{messageId}",
        method = RequestMethod.PATCH
    )
    public ResponseEntity<MessageDto> update(
        @PathVariable("messageId") UUID messageId,
        @RequestBody MessageUpdateRequest request
    ) {
        return ResponseEntity.ok(
            messageService.update(messageId, request)
        );
    }

    @RequestMapping(
            path = "/{messageId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable("messageId") UUID messageId
    ) {
        messageService.delete(messageId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<MessageDto>> findAllByChannelId(
            @RequestParam("channelId") UUID channelId
    ) {
        return ResponseEntity.ok(
                messageService.findAllByChannelId(channelId)
        );
    }
}