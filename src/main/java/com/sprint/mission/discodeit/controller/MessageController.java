package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Tag(name="Message", description = "Message API")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
    private final BasicMessageService messageService;

    public MessageController(BasicMessageService messageService) {
        this.messageService = messageService;
    }

    // 생성
    @Operation(summary = "Message 생성", operationId = "create_2")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨")
    })
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<MessageResponse> createMessage(
            // ModelAttribute -> RequestPart
            @RequestPart("messageCreateRequest") MessageCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> files
            ) throws IOException {
        List<String> fileNames = new ArrayList<>();

        List<BinaryContentCreateRequest> contentList = new ArrayList<>();

        // 여러 장을 순회하며 한 장씩 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                Path savePath = Paths.get("./uploads/" + fileName);
                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath);
                fileNames.add(fileName);

                BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );

                contentList.add(binaryContentCreateRequest);
            }
        }

        Message message = messageService.create(request, contentList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.from(message));
    }
    // 조회
    @Operation(summary = "Message 내용 조회", operationId = "")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message 조회 성공")
    })
    @RequestMapping(value="/{message-id}", method=RequestMethod.GET)
    public ResponseEntity<MessageResponse> getMessage(
            @PathVariable ("message-id") UUID messageId
    ) {
        Message message = messageService.find(messageId);
        return ResponseEntity.ok(MessageResponse.from(message));
    }

    @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
    })
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> getMessages(
            @RequestParam ("channelId") UUID channelId
    ) {
        List<MessageResponse> messages = messageService.findAllByChannelId(channelId).stream()
                .map(MessageResponse::from).toList();
        return ResponseEntity.ok(messages);
    }
    // 수정
    @Operation(summary = "Message 내용 수정", operationId = "update_2")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Message가 성공적으로 수정됨"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
    })
    @RequestMapping(value="/{message-id}", method=RequestMethod.PATCH)
    public ResponseEntity<MessageResponse> updateMessage(
            @Valid @RequestBody MessageUpdateRequest request,
            @PathVariable ("message-id") UUID messageId
    ) {
        Message update = messageService.update(messageId, request);
        return ResponseEntity.ok(MessageResponse.from(update));
    }
    // 삭제
    @Operation(summary = "Message 삭제", operationId = "delete_1")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
    })
    @RequestMapping(value="/{message-id}", method=RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteMessage(
            @PathVariable ("message-id") UUID messageId
    ) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }



}
