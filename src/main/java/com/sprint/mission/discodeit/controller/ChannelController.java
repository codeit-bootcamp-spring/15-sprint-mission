package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // 1. 공개 채널 생성 (POST /api/channels/public)
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDto> createPublicChannel(@RequestBody PublicChannelCreateDto dto) {
        ChannelResponseDto response = channelService.createPublicChannel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 비공개 채널 생성 (POST /api/channels/private)
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponseDto> createPrivateChannel(@RequestBody PrivateChannelCreateDto dto) {
        ChannelResponseDto response = channelService.createPrivateChannel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 3. 채널 단건 조회 (GET /api/channels/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChannelResponseDto> find(@PathVariable("id") UUID id) {
        ChannelResponseDto response = channelService.find(id);
        return ResponseEntity.ok(response);
    }

    // 4. 특정 사용자가 볼 수 있는 채널 목록 조회 (GET /api/channels?userId={userId})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponseDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ChannelResponseDto> response = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // 5. 공개 채널 정보 수정 (PATCH /api/channels/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<ChannelResponseDto> update(
            @PathVariable("id") UUID id,
            @RequestBody ChannelUpdateDto dto) {
        // ChannelUpdateDto 안에 id 필드가 포함되어야 한다면 주입 (예: dto.setId(id))
        ChannelResponseDto response = channelService.update(dto);
        return ResponseEntity.ok(response);
    }

    // 6. 채널 삭제 (DELETE /api/channels/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}