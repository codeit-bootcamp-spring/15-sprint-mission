package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentFindController {

    private final BinaryContentRepository binaryContentRepository;

    public BinaryContentFindController(
            BinaryContentRepository binaryContentRepository
    ) {
        this.binaryContentRepository = binaryContentRepository;
    }

    @RequestMapping(
            path = "/find",
            method = RequestMethod.GET
    )
    public ResponseEntity<BinaryContent> find(
            @RequestParam("binaryContentId") UUID binaryContentId
    ) {
        BinaryContent binaryContent = binaryContentRepository
                .findById(binaryContentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "BinaryContent를 찾을 수 없습니다. id: "
                                        + binaryContentId
                        )
                );

        return ResponseEntity.ok(binaryContent);
    }
}
