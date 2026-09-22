package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.Channel;
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
@RequestMapping("/api/channels")
@AllArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @RequestMapping(path = "/public", method = RequestMethod.POST)
  public ResponseEntity<Channel> createChannel(
      @RequestBody ChannelCreateRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(request));
  }

  @RequestMapping(path = "/private", method = RequestMethod.POST)
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(request.participantIds()));
  }

  @RequestMapping(path = "/{channelId}", method = RequestMethod.PATCH)
  public ResponseEntity<Channel> updatePublicChannel(
      @PathVariable("channelId") UUID id,
      @RequestBody PublicChannelUpdateRequest body
  ) {
    ChannelUpdateRequest request = new ChannelUpdateRequest(id, body.newName(), body.newDescription());
    return ResponseEntity.ok().body(channelService.update(request));
  }

  @RequestMapping(path = "/{channelId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteChannel(@PathVariable("channelId") UUID id) {
    channelService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ChannelResponse>> getChannels(@RequestParam("userId") UUID userId) {
    return ResponseEntity.ok().body(channelService.findAllByUserId(userId));
  }
}
