package com.sprint.mission.discodeit.controller;
import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    /*@PostMapping
    public ResponseEntity<BinaryContent> create(
            @RequestParam("file") MultipartFile file) throws IOException {

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );

        BinaryContent binaryContent = binaryContentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(binaryContent);
    }*/

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
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable("binaryContentId") UUID binaryContentId){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContent);
    }




    @Operation(
            summary = "여러 첨부 파일 조회",
            operationId = "findAllByIdIn"
    )
    @GetMapping
    public ResponseEntity<List<BinaryContent>> getBinaryContentList(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        List<BinaryContent> binaryContents = binaryContentService.findAllByIds(binaryContentIds);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
    }

}
