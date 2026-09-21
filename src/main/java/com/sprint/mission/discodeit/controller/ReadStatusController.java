package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@AllArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody ReadStatusCreateRequest request) {

    ReadStatus readStatus = readStatusService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @RequestMapping(path = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {

    ReadStatus readStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity.ok().body(readStatus);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatus>> getReadStatus(@RequestParam("userId") UUID userId) {
    return ResponseEntity.ok().body(readStatusService.findAllByUserId(userId));
  }

}

/* 읽음 상태 수정만 보류했습니다. 현재는 채널에서 최근 읽음 기록을 골라 수정하지만, 명세는 readStatusId로 지정한 기록을 수정하므로 대상 선택 로직이 달라집니다.*/