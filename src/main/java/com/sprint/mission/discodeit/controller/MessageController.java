package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class MessageController {

  public static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
  private final MessageService messageService;

  @Tag(name = "메세지 생성")
  @PostMapping
  public ResponseEntity<MessageDto> createMessage(
      @Valid @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestParam(required = false) List<MultipartFile> attachments) throws IOException {
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
      throw new IllegalArgumentException("파일이 비었습니다.");
    }
    String fileName = multipartFile.getOriginalFilename();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
    }
    String contentType = multipartFile.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
    }
  }

  @Tag(name = "메세지 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @Valid @ModelAttribute MessageUpdateRequest request) {
    MessageDto message = messageService.update(messageId, request);
    return ResponseEntity.ok().body(message);
  }

  @Tag(name = "채널 전체 메시지 호출")
  @GetMapping()
  public ResponseEntity<PageResponse<MessageDto>> findAllInChannel(
      @RequestParam("channelId") UUID channelId,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable) {
    PageResponse<MessageDto> list = messageService.findAllByChannelId(channelId, pageable);
    return ResponseEntity.ok().body(list);
  }

  @Tag(name = "메세지 삭제")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }


}
