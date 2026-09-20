package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/channels")
public class ChannelController {
    private final BasicChannelService channelService;

    public ChannelController(BasicChannelService channelService) {
        this.channelService = channelService;
    }

    // 등록
    @RequestMapping(value="/public", method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(
            @Valid @ModelAttribute PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelService.create(publicChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ChannelResponse.from(channel)));
    }

    @RequestMapping(value="/private", method=RequestMethod.POST)
    public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(
            @Valid @ModelAttribute PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelService.create(privateChannelCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ChannelResponse.from(channel)));
    }

    // 조회
    @RequestMapping(value="/{channel-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<ChannelResponse>> getChannel(
            @PathVariable("channel-id") UUID channelId
            ){
        ChannelDto channelDto = channelService.find(channelId);
        return ResponseEntity.ok(ApiResponse.success(ChannelResponse.from(channelDto)));
    }

    @RequestMapping(value="/{user-id}", method=RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ChannelResponse>>> getChannels(
            @PathVariable("user-id") UUID userId
    ){
        List<ChannelResponse> channels = channelService.findAllByUserId(userId)
                .stream().map(ChannelResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(channels));
    }

    // 수정
    @RequestMapping(value="/{channel-id}", method=RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<ChannelResponse>> updateChannel(
            @PathVariable ("channel-id") UUID channelId,
            @Valid @ModelAttribute PublicChannelUpdateRequest request
            ) {
        Channel update = channelService.update(channelId, request);
        return ResponseEntity.ok(ApiResponse.success(ChannelResponse.from(update)));
    }
    // 삭제
    @RequestMapping(value="/{channel-id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(
            @PathVariable ("channel-id") UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

}
