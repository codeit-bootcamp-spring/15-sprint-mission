package com.sprint.mission.discodeit.controller;
import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
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


    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable("binaryContentId") UUID binaryContentId){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContent);
    }


    @GetMapping
    public ResponseEntity<List<BinaryContent>> getBinaryContentList(@RequestParam("binaryContentIds") List<UUID> binaryContentIds){
        List<BinaryContent> binaryContents = binaryContentService.findAllByIds(binaryContentIds);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
    }

}
