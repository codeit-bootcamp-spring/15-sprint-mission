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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @Tag(name = "첨부파일 정보 호출")
    @GetMapping("/api/binaryContent/find")
    public ResponseEntity<BinaryContent> getBinaryContent(@Valid @RequestParam("binaryContentId") UUID binaryContentId){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok().body(binaryContent);
    }

    @Tag(name = "첨부파일 다운로드")
    @GetMapping("/download/{binaryContentId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        String name = binaryContent.getFileName();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+name) // 파일 다운로드 ( 없을시 화면 띄우기 )
                .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .body(binaryContent.getBytes());
    }
}
