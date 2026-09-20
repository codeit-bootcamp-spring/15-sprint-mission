package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> createMessage(@RequestBody MessageCreateRequest messageCreateRequest){
        Message message = messageService.create(messageCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(message));
    }

    //요구사항에서 현제단계의 메세지 수정은 첨부된 바이너리컨텐츠는 수정하지 않고 String만 수정하라고 했습니다.
    //그래서 텍스트를 지우고 사진만 있는 메세지로 변경하려는 케이스를 생각해 null방어를 하지 않았습니다.
    @PatchMapping("/{message-id}")
    public ResponseEntity<ApiResponse<Message>> patchMessage(@PathVariable("message-id") UUID id ,
                                                             @RequestBody MessageUpdateRequest messageUpdateRequest){
        Message message = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(message));
    }


    @DeleteMapping("/{message-id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable("message-id") UUID uuid) {
        messageService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    //채널id로 해당 채널의 메세지 목록 조회
    @GetMapping("/by-channel-id/{channel-id}")
    public ResponseEntity<ApiResponse<List<Message>>> getAllByChannelId(@PathVariable("channel-id") UUID channelId){
        List<Message> messages = messageService.findAllByChannelId(channelId);

        return ResponseEntity.ok(ApiResponse.success(messages));
    }
}
