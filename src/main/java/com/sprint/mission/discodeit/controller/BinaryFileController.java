package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryFileController {
    private final BinaryContentService binaryContentService;

    // 단일 조회
    @GetMapping(path = "{binaryContentId}")
    public ResponseEntity<BinaryContent> find(@PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContent);
    }

    // id 조회
    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(binaryContents);
    }
}
