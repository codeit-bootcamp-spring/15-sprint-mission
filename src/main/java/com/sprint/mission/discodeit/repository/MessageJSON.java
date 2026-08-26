package com.sprint.mission.discodeit.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

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


    public static void LoadMessage() {
        MessageData messageData;
        try {
            messageData = mapper.readValue(new File("message.json"),
                    new TypeReference<MessageData>() {});

        }catch (IOException e) {
            e.printStackTrace();
            messageData=null;

        }finally {

        }
        JCFMessageService.getInstance().setMessageMap(messageData.getMessageMap());
        JCFMessageService.getInstance().setReactionMap(messageData.getReactionMap());

    }


}
