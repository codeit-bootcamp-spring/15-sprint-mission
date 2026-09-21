package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(
            method = RequestMethod.POST
    )
    public ResponseEntity<ReadStatusDto> create(
            @RequestBody ReadStatusCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readStatusService.create(request));
    }

    @RequestMapping(
            method = RequestMethod.PATCH
    )
    public ResponseEntity<ReadStatusDto> update(
            @RequestBody ReadStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                readStatusService.update(request)
        );
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
            @RequestParam("userId") UUID userId
    ) {
        return ResponseEntity.ok(
                readStatusService.findAllByUserId(userId)
        );
    }
}