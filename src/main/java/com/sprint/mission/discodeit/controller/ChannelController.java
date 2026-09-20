package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/public")
    public ResponseEntity<ChannelResponse> createPublicChannel(
            @Valid @RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelResponse> createPrivateChannel(
            @Valid @RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChannelResponse.from(channel));
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(
            @PathVariable UUID channelId,
            @Valid @RequestBody PublicChannelUpdateRequest request) {
        Channel channel = channelService.update(channelId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ChannelResponse.from(channel));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> deleteChannel(
            @PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelResponse>> getChannelsByUserId(
            @PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        List<ChannelResponse> responses = channels.stream()
                .map(ChannelResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
