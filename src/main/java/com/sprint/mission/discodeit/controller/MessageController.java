package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/messages")
public class MessageController {
    private final BasicMessageService messageService;

    public MessageController(BasicMessageService messageService) {
        this.messageService = messageService;
    }

    // 생성
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<MessageResponse>> createMessage(
            @Valid @ModelAttribute MessageCreateRequest request,
            List<BinaryContentCreateRequest> binaryContentCreateRequest
            ) {
        Message message = messageService.create(request, binaryContentCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MessageResponse.from(message)));
    }
    // 조회
    @RequestMapping(value="/{message-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<MessageResponse>> getMessage(
            @PathVariable ("message-id") UUID messageId
    ) {
        Message message = messageService.find(messageId);
        return ResponseEntity.ok(ApiResponse.success(MessageResponse.from(message)));
    }

    @RequestMapping(value="/{channel-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
            @PathVariable ("channel-id") UUID channelId
    ) {
        List<MessageResponse> messages = messageService.findAllByChannelId(channelId).stream()
                .map(MessageResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(messages));
    }
    // 수정
    @RequestMapping(value="/{message-id}", method=RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessage(
            @Valid @ModelAttribute MessageUpdateRequest request,
            @PathVariable ("message-id") UUID messageId
    ) {
        Message update = messageService.update(messageId, request);
        return ResponseEntity.ok(ApiResponse.success(MessageResponse.from(update)));
    }
    // 삭제
    @RequestMapping(value="/{message-id}", method=RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<MessageResponse>> deleteMessage(
            @PathVariable ("message-id") UUID messageId
    ) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }



}
