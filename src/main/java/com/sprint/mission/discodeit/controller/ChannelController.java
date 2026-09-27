package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.*;
import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    //공개 채널 생성
    @ApiResponse(
            responseCode = "201",
            description = "공개 채널 등록 성공"
    )
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.toChannelResponse(channel));
    }

    //비공개 채널 생성
    @ApiResponse(
            responseCode = "201",
            description = "비공개 채널 등록 성공"
    )
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.toChannelResponse(channel));
    }

    //업데이트(공개 채널만 가능)
    @PatchMapping("/{channel-id}")
    public ResponseEntity<ChannelDto> patchChannel(
            @PathVariable("channel-id") UUID uuid, @RequestBody ChannelUpdateRequest channelUpdateRequest) {



        Channel channel = channelService.update(uuid, channelUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(channelService.toChannelResponse(channel));
    }

    //삭제
    @ApiResponse(
            responseCode = "204",
            description = "채널 삭제 성공"
    )
    @DeleteMapping("/{channel-id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable("channel-id") UUID uuid) {
        channelService.delete(uuid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    //
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllByUserId( @RequestParam("userId") UUID userId){
        List<ChannelDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }

}
