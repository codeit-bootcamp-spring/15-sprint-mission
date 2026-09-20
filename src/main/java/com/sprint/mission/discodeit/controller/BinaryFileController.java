package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryFileController {
    private final BinaryContentService binaryContentService;

    // 단일 조회
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(
            @PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }

    // 여러개 조회
    @GetMapping
    public ResponseEntity<List<BinaryContent>> getBinaryContents(
            @RequestParam List<UUID> ids) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(binaryContents);
    }

    // 다운로드
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<byte[]> downloadBinaryContent(
            @PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + binaryContent.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .contentLength(binaryContent.getSize())
                .body(binaryContent.getBytes());
    }

    // 심화 요구사항
    // 파일 조회
    @GetMapping("/find")
    public ResponseEntity<BinaryContent> findBinaryContent(
            @RequestParam UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }


}
