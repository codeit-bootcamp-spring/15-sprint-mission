package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent; // 패키지 경로 확인 후 임포트
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    // [심화 요구사항] BinaryContent 파일 조회
    // url: /api/binaryContent/find?binaryContentId={id}
    @GetMapping("/find")
    public ResponseEntity<BinaryContent> find(@RequestParam("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }
}