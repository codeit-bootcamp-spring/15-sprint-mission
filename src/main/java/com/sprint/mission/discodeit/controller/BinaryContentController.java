package com.sprint.mission.discodeit.controller;
import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @Operation(
            summary = "첨부 파일 조회",
            operationId = "find"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 조회 성공"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "파일을 찾을 수 없음"
            )
    })
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable("binaryContentId") UUID binaryContentId){
        BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContentDto);
    }




    @Operation(
            summary = "여러 첨부 파일 조회",
            operationId = "findAllByIdIn"
    )
    @ApiResponse(
            responseCode = "200",
            description = "파일리스트 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> getBinaryContentList(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        List<BinaryContentDto> binaryContentDtos = binaryContentService.findAllByIds(binaryContentIds);


        return ResponseEntity.status(HttpStatus.OK).body(binaryContentDtos);
    }

    // todo 다운로드 추가 api/binaryContents/{binaryContentId}/download

    @GetMapping("{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId){
        return binaryContentService.download(binaryContentId);
    }
}
