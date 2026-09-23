package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;


    // 메시지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ReadStatus create(
            @RequestBody ReadStatusCreateRequest request
    ) {

        return readStatusService.create(request);
    }


    // 메시지 수신 정보 수정
    @RequestMapping(
            path = "/{readStatusId}",
            method = RequestMethod.PATCH
    )
    public ReadStatus update(
            @PathVariable("readStatusId") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request
    ) {

        return readStatusService.update(
                readStatusId,
                request
        );
    }


    // 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ReadStatus> findAllByUserId(
            @RequestParam("userId") UUID userId
    ) {

        return readStatusService.findAllByUserId(
                userId
        );
    }
}