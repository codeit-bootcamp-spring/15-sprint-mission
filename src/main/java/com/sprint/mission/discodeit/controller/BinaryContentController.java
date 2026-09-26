package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name="BinaryContent", description = "첨부 파일 API")
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BasicBinaryContentService basicBinaryContentService;

    public BinaryContentController(BasicBinaryContentService basicBinaryContentService) {
        this.basicBinaryContentService = basicBinaryContentService;
    }
    // 바이너리 파일 단건 조회
//    @RequestMapping(value="/{content-id}", method=RequestMethod.GET)
//    public ResponseEntity<ApiResponse<BinaryContentResponse>> getContent(
//            @PathVariable("content-id") UUID contentId) {
//        BinaryContent content = basicBinaryContentService.find(contentId);
//        return ResponseEntity.ok(ApiResponse.success(BinaryContentResponse.from(content)));
//    }
    // 정적 리소스 서빙
    @Operation(summary = "첨부 파일 조회", operationId = "find")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "첨부파일을 찾을 수 없음"),
        })
    @RequestMapping(value="/{binaryContentId}", method=RequestMethod.GET)
    public ResponseEntity<BinaryContent> getContent(
            @PathVariable("binaryContentId") UUID contentId) {
        BinaryContent content = basicBinaryContentService.find(contentId);
        return ResponseEntity.ok(content);
    }
    @Operation(summary = "여러 첨부 파일 조회", operationId = "findAllByIdIn")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공"),
    })
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
