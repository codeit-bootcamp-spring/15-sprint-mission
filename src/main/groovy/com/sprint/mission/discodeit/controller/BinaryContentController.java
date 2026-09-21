package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    @RequestMapping(
            path = "/{binaryContentId}",
            method = RequestMethod.GET
    )
    public ResponseEntity<BinaryContentDto> find(
            @PathVariable("binaryContentId") UUID binaryContentId
    ) {
        return ResponseEntity.ok(
                binaryContentService.find(binaryContentId)
        );
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
            @RequestParam("ids") List<UUID> ids
    ) {
        return ResponseEntity.ok(
                binaryContentService.findAllByIdIn(ids)
        );
    }
}