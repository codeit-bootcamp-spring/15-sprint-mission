package com.sprint.mission.discodeit.FileIO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.File;
import java.io.IOException;

public class MessageJSON {
    private static final ObjectMapper mapper = new ObjectMapper();



    public static void SaveMessage() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("message.json"), new MessageData(
                            JCFMessageService.getInstance().getMessageMap()
                            ,JCFMessageService.getInstance().getReactionMap()));

            System.out.println("메세지 저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
