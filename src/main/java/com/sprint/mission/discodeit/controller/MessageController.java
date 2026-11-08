package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.binaryContent.EmptyFileException;
import com.sprint.mission.discodeit.exception.binaryContent.InvalidImageContentTypeException;
import com.sprint.mission.discodeit.exception.binaryContent.MissingFileNameException;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Slf4j
public class MessageController {

  public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
  private final MessageService messageService;

  @Tag(name = "메세지 생성")
  @PostMapping
  public ResponseEntity<MessageDto> createMessage(
      @Valid @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestParam(required = false) List<MultipartFile> attachments) throws IOException {
    log.debug("메세지 생성 API 요청 : authorId = {}, ChannelId = {}, content = {}, attachments = {}",
        request.authorId(), request.channelId(), request.content(),
        attachments == null ? 0 : attachments.size());
    List<BinaryContentCreateRequest> profile = new ArrayList<>();
    if (attachments != null && !attachments.isEmpty()) {
      for (MultipartFile file : attachments) {
        validateImageFile(file);
        byte[] photo = file.getBytes();
        String fileName = file.getOriginalFilename();
        String extension = file.getContentType();
        profile.add(new BinaryContentCreateRequest(fileName, extension, photo));
      }
    }
    MessageDto message = messageService.create(request, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  @Tag(name = "메세지 첨부파일 검증")
  private void validateImageFile(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new EmptyFileException();
    }
    String fileName = multipartFile.getOriginalFilename();
    if (fileName == null || fileName.isBlank()) {
      throw new MissingFileNameException();
    }
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    String contentType = multipartFile.getContentType();
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new InvalidImageContentTypeException(extension, contentType);
    }
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new InvalidImageContentTypeException(extension, contentType);
    }
  }

  @Tag(name = "메세지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @Valid @RequestBody MessageUpdateRequest request) {
    log.debug("메세지 수정 API 요청 : messageId = {}", messageId);
    MessageDto message = messageService.update(messageId, request);
    log.debug("메세지 수정 API 완료 : messageId = {}, newContent = {}", message.id(), message.content());
    return ResponseEntity.ok().body(message);
  }

  @Tag(name = "채널 전체 메시지 호출")
  @GetMapping()
  public ResponseEntity<PageResponse<MessageDto>> findAllInChannel(
      @RequestParam("channelId") UUID channelId,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable, @RequestParam(value = "cursor", required = false) Instant cursor) {
    PageResponse<MessageDto> list = messageService.findAllByChannelId(channelId, cursor, pageable);
    return ResponseEntity.ok().body(list);
  }

  @Tag(name = "메세지 삭제")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID messageId) {
    log.debug("메세지 삭제 API 요청 : messageId = {}", messageId);
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }


}
