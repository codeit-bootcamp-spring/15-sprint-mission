package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Tag(name = "Message", description = "메시지 관련 API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Operation(summary = "메시지 생성(전송)", description = "채널에 새로운 메시지를 전송합니다.")
  @PostMapping
  public ResponseEntity<MessageDto> create(
      @RequestPart(value = "messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateDto> attachmentDtos = new ArrayList<>();
    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          try {
            attachmentDtos.add(new BinaryContentCreateDto(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType()
            ));
          } catch (IOException e) {
            throw new RuntimeException("첨부파일 변환 실패", e);
          }
        }
      }
    }

    MessageCreateDto dto = new MessageCreateDto(
        request.content(),
        request.channelId(),
        request.authorId(),
        attachmentDtos
    );

    MessageDto response = messageService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "채널별 메시지 목록 조회", description = "특정 채널에 속한 메시지 목록을 최근순 50개 페이징 조회합니다.")
  @GetMapping
  public ResponseEntity<?> findAllByChannelId(
      @RequestParam(value = "channelId", required = false) UUID channelId,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    // channelId가 없을 때 4개 인수로 빈 PageResponse 생성
    if (channelId == null) {
      return ResponseEntity.ok(
          new PageResponse<>(List.of(), pageable.getPageNumber(), pageable.getPageSize(), 0L));
    }

    PageResponse<MessageDto> response = messageService.findAllByChannelId(channelId, pageable);
    return ResponseEntity.ok(response.content());
  }

  @Operation(summary = "메시지 단건 조회", description = "메시지 ID로 단건 메시지를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<MessageDto> find(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(messageService.find(id));
  }

  @Operation(summary = "메시지 수정", description = "메시지 내용을 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<MessageDto> update(
      @PathVariable("id") UUID id,
      @RequestBody MessageUpdateDto dto) {
    return ResponseEntity.ok(messageService.update(id, dto));
  }

  @Operation(summary = "메시지 삭제", description = "메시지 ID로 메시지를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    messageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}