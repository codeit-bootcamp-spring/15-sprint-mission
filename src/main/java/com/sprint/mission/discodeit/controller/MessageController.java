package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.exception.BinaryRequestFailedException;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Message", description = "Message API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final Validator validator;


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
    public ResponseEntity<MessageDto> createMessage(
            @RequestPart("messageCreateRequest") @Valid MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ){
        log.debug("메세지 보내기 요청");
        List<BinaryContentCreateRequest> attachmentRequests =
                attachments == null
                        ? new ArrayList<>()
                        : attachments.stream()
                        .map(file -> {
                            try {
                                BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                                var violations = validator.validate(request);
                                if (!violations.isEmpty()) {
                                    throw new ConstraintViolationException(violations);
                                }
                                return request;
                            } catch (IOException e) {
                                throw new BinaryRequestFailedException(e,file.getOriginalFilename());
                            }
                        })
                        .toList();

        MessageDto messageDto = messageService.create(messageCreateRequest,attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageDto);
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
    public ResponseEntity<MessageDto> patchMessage(@PathVariable("message-id") UUID id ,
                                                             @RequestBody @Valid MessageUpdateRequest messageUpdateRequest){
        log.debug("메세지 수정 요청");
        MessageDto messageDto = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
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
        log.debug("메세지 삭제 요청");
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
    public PageResponse<MessageDto> getAllByChannelId(
            @RequestParam UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @PageableDefault(
                    size = 50,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return messageService.findAllByChannelId(
                channelId, cursor, pageable
        );
    }
}
