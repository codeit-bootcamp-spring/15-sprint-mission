package com.sprint.mission.discodeit.controller;
import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
@RequestMapping("/api/binaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    //이번 미션에서 파일관련 요구사항은 Get뿐이라 검증용으로 post는 ai한테 만들어 달라고 했습니다.
    @PostMapping
    public ResponseEntity<ApiResponse<BinaryContent>> create(
            @RequestParam("file") MultipartFile file) throws IOException {

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getBytes()
        );

        BinaryContent binaryContent = binaryContentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(binaryContent));
    }


    @GetMapping("/find")
    public ResponseEntity<ApiResponse<BinaryContent>> getBinaryContent(@RequestParam("binaryContentId") UUID binaryContentId){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.ok(ApiResponse.success(binaryContent));
    }


    @GetMapping("/ids")
    public ResponseEntity<ApiResponse<List<BinaryContent>>> getBinaryContentList(@RequestParam List<UUID> ids){
        List<BinaryContent> binaryContents = binaryContentService.findAllByIds(ids);

        return ResponseEntity.ok(ApiResponse.success(binaryContents));
    }

}
