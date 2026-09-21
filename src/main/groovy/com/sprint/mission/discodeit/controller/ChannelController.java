package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(
            path = "/public",
            method = RequestMethod.POST
    )
    public ResponseEntity<ChannelDto> createPublic(
            @RequestBody PublicChannelCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channelService.createPublic(request));
    }

    @RequestMapping(
            path = "/private",
            method = RequestMethod.POST
    )
    public ResponseEntity<ChannelDto> createPrivate(
            @RequestBody PrivateChannelCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channelService.createPrivate(request));
    }

    @RequestMapping(
            method = RequestMethod.PATCH
    )
    public ResponseEntity<ChannelDto> update(
            @RequestBody ChannelUpdateRequest request
    ) {
        return ResponseEntity.ok(
                channelService.update(request)
        );
    }

    @RequestMapping(
            path = "/{channelId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable("channelId") UUID channelId
    ) {
        channelService.delete(channelId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ChannelDto>> findAllByUserId(
            @RequestParam("userId") UUID userId
    ) {
        return ResponseEntity.ok(
                channelService.findAllByUserId(userId)
        );
    }
}