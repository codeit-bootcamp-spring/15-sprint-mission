package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Tag(name = "첨부파일 정보 호출")
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok().body(binaryContent);
  }

  @Tag(name = "첨부파일 다운로드")
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto dto = binaryContentService.find(binaryContentId);
    return binaryContentStorage.download(dto);
  }

  @Tag(name = "첨부파일 여러 개 조회")
  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentDto> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok().body(contents);
  }
}
