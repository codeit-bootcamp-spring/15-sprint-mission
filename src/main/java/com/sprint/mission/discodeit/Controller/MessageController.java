package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
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


    // 메시지 전송
    @RequestMapping(method = RequestMethod.POST)
    public Message create(
            @RequestBody MessageCreateApiRequest request
    ) {

        return messageService.create(
                request.message(),
                request.attachments()
        );
    }


    // 특정 채널의 메시지 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> findAllByChannelId(
            @RequestParam("channelId") UUID channelId
    ) {

        return messageService.findAllByChannelId(
                channelId
        );
    }


    // 메시지 수정
    @RequestMapping(
            path = "/{messageId}",
            method = RequestMethod.PATCH
    )
    public Message update(
            @PathVariable("messageId") UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {

        return messageService.update(
                messageId,
                request
        );
    }


    // 메시지 삭제
    @RequestMapping(
            path = "/{messageId}",
            method = RequestMethod.DELETE
    )
    public void delete(
            @PathVariable("messageId") UUID messageId
    ) {

        messageService.delete(messageId);
    }


    // 메시지 + 첨부파일을 하나의 요청으로 받기 위한 묶음
    public record MessageCreateApiRequest(
            MessageCreateRequest message,
            List<BinaryContentCreateRequest> attachments
    ) {
    }
}