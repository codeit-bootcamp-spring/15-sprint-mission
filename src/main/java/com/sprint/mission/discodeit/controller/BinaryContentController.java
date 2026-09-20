package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@AllArgsConstructor
public class BinaryContentController {
    private final BinaryContentService contentService;

    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getContent(@RequestParam("binaryContentId") UUID binaryContentId) {
        return ResponseEntity.ok().body(contentService.find(binaryContentId));
    }
}
