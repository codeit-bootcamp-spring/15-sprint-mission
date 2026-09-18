package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 1. 메시지 전송 (POST /api/messages)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> create(@RequestBody MessageCreateDto dto) {
        Message response = messageService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 특정 채널의 메시지 목록 조회 (GET /api/messages?channelId={channelId})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId) {
        List<Message> response = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(response);
    }

    // 3. 단건 조회 (GET /api/messages/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Message> find(@PathVariable("id") UUID id) {
        Message response = messageService.find(id);
        return ResponseEntity.ok(response);
    }

    // 4. 메시지 수정 (PATCH /api/messages/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Message> update(
            @PathVariable("id") UUID id,
            @RequestBody MessageUpdateDto dto) {
        // MessageUpdateDto에 id 필드가 포함되는 구조라면 세팅 (예: dto.setId(id))
        Message response = messageService.update(dto);
        return ResponseEntity.ok(response);
    }

    // 5. 메시지 삭제 (DELETE /api/messages/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}