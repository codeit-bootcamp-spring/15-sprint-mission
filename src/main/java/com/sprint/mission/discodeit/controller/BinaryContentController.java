package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Tag(name = "첨부파일 정보 호출")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<ApiResponse<BinaryContent>> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok().body(ApiResponse.success(binaryContent));
  }

  @Tag(name = "첨부파일 다운로드")
  @GetMapping("/download/{binaryContentId}")
  public ResponseEntity<byte[]> getImage(@PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    String name = binaryContent.getFileName();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + name) // 파일 다운로드 ( 없을시 화면 띄우기 )
        .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
        .body(binaryContent.getBytes());
  }

  @Tag(name = "첨부파일 여러 개 조회")
  @GetMapping
  public ResponseEntity<ApiResponse<List<BinaryContent>>> getBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok().body(ApiResponse.success(contents));
  }
}
