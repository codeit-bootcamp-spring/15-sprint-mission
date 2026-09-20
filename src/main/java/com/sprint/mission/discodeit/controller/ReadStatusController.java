package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readstatus")
@AllArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;
    // todo: 특정 채널의 메시지 수신 정보를 생성할 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(
            @RequestParam("user-id") UUID userId,
            @RequestParam("channel-id") UUID channelId) {
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId);

        ReadStatus readStatus = readStatusService.create(request);

        return ResponseEntity.ok().body(readStatus);
    }

    // todo:> 특정 채널의 메시지 수신 정보를 수정할 수 있다.
    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateReadStatus(
            @RequestParam("channel-id") UUID channelId)
    {
        ReadStatusUpdateRequest request =
                new ReadStatusUpdateRequest(channelId, Instant.now());

        readStatusService.update(request);
        return ResponseEntity.noContent().build();
    }

    // todo: 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    @RequestMapping(path = "/{user-id}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getReadStatus(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok().body(readStatusService.findAllByUserId(userId));
    }

}
