package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@AllArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(path = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createChannel(
            @RequestParam("name") String name,
            @RequestParam("description") String description) {
        ChannelCreateRequest request = new ChannelCreateRequest(name, description);

        return ResponseEntity.ok()
                .body(channelService.find(
                        channelService.createPublicChannel(request).getId()));
    }

    @RequestMapping(path = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPrivateChannel(@RequestParam("ids") List<UUID> ids) {
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ids);

        return ResponseEntity.ok()
                .body(channelService.find(
                        channelService.createPrivateChannel(request.participantIds()).getId()));
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<ChannelResponse> updatePublicChannel(
            @RequestParam("id") UUID id,
            @RequestParam("name") String name,
            @RequestParam("description") String description
    ) {
        ChannelUpdateRequest request = new ChannelUpdateRequest(id, name, description);
        return ResponseEntity.ok().body(channelService.update(request));
    }

    @RequestMapping(path = "/{channel-id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable("channel-id") UUID id) {
        channelService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(path = "/{user-id}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponse>> getChannels(@PathVariable("user-id") UUID userId) {
        return ResponseEntity.ok().body(channelService.findAllByUserId(userId));
    }
}
