package com.sprint.mission.discodeit.FileIO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.File;
import java.io.IOException;
import java.util.Set;


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



}
