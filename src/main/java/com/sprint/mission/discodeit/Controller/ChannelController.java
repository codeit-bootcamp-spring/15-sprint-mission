package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.ChannelDto.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.ChannelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;


    // 공개 채널 생성
    @RequestMapping(
            path = "/public",
            method = RequestMethod.POST
    )
    public Channel createPublic(
            @RequestBody PublicChannelCreateRequest request
    ) {

        return channelService.createPublic(request);
    }


    // 비공개 채널 생성
    @RequestMapping(
            path = "/private",
            method = RequestMethod.POST
    )
    public Channel createPrivate(
            @RequestBody PrivateChannelCreateRequest request
    ) {

        return channelService.createPrivate(request);
    }


    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelFindResponse> findAllByUserId(
            @RequestParam("userId") UUID userId
    ) {

        return channelService.findAllByUserId(userId);
    }


    // 공개 채널 수정
    @RequestMapping(
            path = "/{channelId}",
            method = RequestMethod.PATCH
    )
    public Channel update(
            @PathVariable("channelId") UUID channelId,
            @RequestBody ChannelUpdateRequest request
    ) {

        return channelService.update(
                channelId,
                request
        );
    }


    // 채널 삭제
    @RequestMapping(
            path = "/{channelId}",
            method = RequestMethod.DELETE
    )
    public void delete(
            @PathVariable("channelId") UUID channelId
    ) {

        channelService.delete(channelId);
    }
}