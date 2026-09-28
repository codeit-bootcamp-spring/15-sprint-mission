package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;


    @Operation(
            summary = "Message 생성",
            operationId = "create_2"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "메세지 등록 성공"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "채널 또는 유저 없음"
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ){
        List<BinaryContentCreateRequest> attachmentRequests =
                attachments == null
                        ? new ArrayList<>()
                        : attachments.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        })
                        .toList();

        Message message = messageService.create(messageCreateRequest,attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @Operation(
            summary = "Message 내용 수정",
            operationId = "update_2"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "메세지 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메세지를 찾을 수 없음"
            )
    })
    @PatchMapping("/{message-id}")
    public ResponseEntity<Message> patchMessage(@PathVariable("message-id") UUID id ,
                                                             @RequestBody MessageUpdateRequest messageUpdateRequest){
        Message message = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


    @Operation(
            summary = "Message 삭제",
            operationId = "delete_1"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "메세지 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메세지 찾을 수 없음"
            )
    })
    @DeleteMapping("/{message-id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("message-id") UUID uuid) {
        messageService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //채널id로 해당 채널의 메세지 목록 조회
    @Operation(
            summary = "Channel의 Message 목록 조회",
            operationId = "findAllByChannelId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "채널의 메세지 목록 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<Message>> getAllByChannelId(@RequestParam("channelId") UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);

        return ResponseEntity.ok(messages);
    }
}
