package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateBody;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Message> postMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> files) {
    List<BinaryContentCreateRequest> binaryContents = null;
    if (files != null && !files.isEmpty()) {
      binaryContents = files.stream().map(BinaryContentCreateRequest::from).toList();
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(request, binaryContents));
  }

  @RequestMapping(path = "/{messageId}", method = RequestMethod.PATCH)
  public ResponseEntity<Message> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateBody body) {
    MessageUpdateRequest request = new MessageUpdateRequest(messageId, body.newContent());

    return ResponseEntity.ok().body(messageService.update(request));
  }

  @RequestMapping(path = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") UUID id) {
    messageService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Message>> getMessages(@RequestParam("channelId") UUID channelId) {
    return ResponseEntity.ok().body(messageService.findAllByChannelId(channelId));
  }
}
