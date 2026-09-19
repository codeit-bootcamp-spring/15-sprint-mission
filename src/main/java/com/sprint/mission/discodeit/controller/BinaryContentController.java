package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/content")
public class BinaryContentController {
    private final BasicBinaryContentService basicBinaryContentService;

    public BinaryContentController(BasicBinaryContentService basicBinaryContentService) {
        this.basicBinaryContentService = basicBinaryContentService;
    }
    // 바이너리 파일 단건 조회
    @RequestMapping(value="/{content-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<BinaryContentResponse>> getContent(
            @PathVariable("content-id") UUID contentId) {
        BinaryContent content = basicBinaryContentService.find(contentId);
        return ResponseEntity.ok(ApiResponse.success(BinaryContentResponse.from(content)));
    }

    // 바이너리 파일 여러건 조회
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<BinaryContentResponse>>> getContents(
            @RequestParam("binary content-ids") List<UUID> binaryContendIds
    ) {
        List<BinaryContentResponse> content = basicBinaryContentService.findAllByIdIn(binaryContendIds)
                .stream().map(BinaryContentResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(content));
    }

}
