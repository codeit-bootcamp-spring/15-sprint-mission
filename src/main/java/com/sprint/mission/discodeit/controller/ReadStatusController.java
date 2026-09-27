package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @ApiResponse(
            responseCode = "201",
            description = "ReadStatus 등록 성공"
    )
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readStatus);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> patchReadStatus(@PathVariable("readStatusId") UUID readStatusId){
        ReadStatus readStatus = readStatusService.update(readStatusId);

        return ResponseEntity.ok(readStatus);
    }


    /*@PatchMapping("/by-user-id/{user-id}")
    public ResponseEntity<List<ReadStatus>> patchReadStatusByUserId(@PathVariable("user-id") UUID userId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }

    @PatchMapping("/by-channel-id/{channel-id}")
    public ResponseEntity<List<ReadStatus>> patchReadStatusByChannelId(@PathVariable("channel-id") UUID channelId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }*/

    @GetMapping
    public ResponseEntity<List<ReadStatus>> getReadStatusByUserId( @RequestParam("userId") UUID userId){
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }





}
