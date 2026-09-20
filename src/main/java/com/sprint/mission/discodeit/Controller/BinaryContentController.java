package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binarycontents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;


    // 바이너리 파일 1개 조회
    @RequestMapping(
            path = "/find",
            method = RequestMethod.GET
    )
    public ResponseEntity<BinaryContent> find(
            @RequestParam("binaryContentId") UUID binaryContentId
    ) {

        BinaryContent binaryContent =
                binaryContentService.find(binaryContentId);

        return ResponseEntity.ok(binaryContent);
    }


    // 바이너리 파일 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContent> findAllByIdIn(
            @RequestParam("ids") List<UUID> ids
    ) {

        return binaryContentService.findAllByIdIn(
                ids
        );
    }
}