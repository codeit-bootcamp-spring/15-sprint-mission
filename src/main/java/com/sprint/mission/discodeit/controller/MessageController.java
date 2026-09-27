package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(
            @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ){
        List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());

        Message message = messageService.create(messageCreateRequest,attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }


    @PatchMapping("/{message-id}")
    public ResponseEntity<Message> patchMessage(@PathVariable("message-id") UUID id ,
                                                             @RequestBody MessageUpdateRequest messageUpdateRequest){
        Message message = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


    @DeleteMapping("/{message-id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("message-id") UUID uuid) {
        messageService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //채널id로 해당 채널의 메세지 목록 조회
    @GetMapping("/by-channel-id/{channel-id}")
    public ResponseEntity<List<Message>> getAllByChannelId(@PathVariable("channel-id") UUID channelId){
        List<Message> messages = messageService.findAllByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
