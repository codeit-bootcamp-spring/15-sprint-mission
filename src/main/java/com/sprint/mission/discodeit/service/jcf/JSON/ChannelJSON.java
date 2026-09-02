package com.sprint.mission.discodeit.service.jcf.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
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
                    ,JCFChannelService.getInstance().getChanelMap()));

            System.out.println("채널 저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void LoadChannel() {
        ChannelData channelData;
        try {
            channelData = mapper.readValue(new File("channel.json"),
                    new TypeReference<ChannelData>() {});

        }catch (IOException e) {
            e.printStackTrace();
            channelData=null;

        }finally {

        }
        JCFChannelService.getInstance().setUserRoleMap(channelData.getUserRoleMap());
        JCFChannelService.getInstance().setMessagesListMap(channelData.getMessagesListMap());
        JCFChannelService.getInstance().setChanelMap(channelData.getChannelMap());

    }
}
