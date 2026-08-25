package com.sprint.mission.discodeit.FileIO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.io.File;
import java.io.IOException;

public class ChannelJSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void SaveChannel() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("channel.json"), new ChannelData(
                            JCFChannelService.getInstance().getUserRoleMap()
                    ,JCFChannelService.getInstance().getMessagesListMap()
                    ,JCFChannelService.getInstance().getChannelSet()));

            System.out.println("채널 저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
