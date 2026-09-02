package com.sprint.mission.discodeit.service.jcf.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.File;
import java.io.IOException;


public class UserJSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void SaveUser() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("user.json"), new UserData(JCFUserService.getInstance().getUserMap()));

            System.out.println("유저 저장 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadUser() {
        UserData userData;
        try {
            userData = mapper.readValue(new File("user.json"),
                    new TypeReference<UserData>() {});

        }catch (IOException e) {
            e.printStackTrace();
            userData=null;

        }finally {

        }
        JCFUserService.getInstance().setUserMap(userData.getUserMap());

    }



}
