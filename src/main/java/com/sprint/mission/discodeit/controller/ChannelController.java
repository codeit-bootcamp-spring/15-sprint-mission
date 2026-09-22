package com.sprint.mission.discodeit.controller;
import com.sprint.mission.discodeit.dto.ChannelDto;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelDto> findAll() {
        return channelService.findAll();

    }


}
