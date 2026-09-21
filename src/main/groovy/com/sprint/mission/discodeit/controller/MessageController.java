package com.sprint.mission.discodeit.controller;

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
            method = RequestMethod.POST
    )
    public ResponseEntity<MessageDto> create(
            @RequestBody MessageCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageService.create(request));
    }

    @RequestMapping(
            method = RequestMethod.PATCH
    )
    public ResponseEntity<MessageDto> update(
            @RequestBody MessageUpdateRequest request
    ) {
        return ResponseEntity.ok(
                messageService.update(request)
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