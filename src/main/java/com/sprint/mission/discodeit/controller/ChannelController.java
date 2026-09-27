package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.*;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    //공개 채널 생성
    @PostMapping("/type-public")
    public ResponseEntity<ChannelResponse> createPublicChannel(@Valid @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.toChannelResponse(channel));
    }

    //비공개 채널 생성
    @PostMapping("/type-private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.toChannelResponse(channel));
    }

    //업데이트(공개 채널만 가능)
    @PatchMapping("/{channel-id}")
    public ResponseEntity<ChannelResponse> patchChannel(
            @PathVariable("channel-id") UUID uuid,@Valid @RequestBody ChannelUpdateRequest channelUpdateRequest) {



        Channel channel = channelService.update(uuid, channelUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(channelService.toChannelResponse(channel));
    }

    //삭제
    @DeleteMapping("/{channel-id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable("channel-id") UUID uuid) {
        channelService.delete(uuid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    //
    @GetMapping("/by-user-id/{user-id}")
    public ResponseEntity<List<ChannelResponse>> getAllByUserId(@PathVariable("user-id") UUID userId){
        List<ChannelResponse> channels = channelService.findAllByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }

}
