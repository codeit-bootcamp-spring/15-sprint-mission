package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        BasicUserService.getInstance().setUserRepository(FileUserRepository.getInstance());
        User user = new User("as@das.ds","qwer","qwer", NitroLevel.BASIC);
        FileUserRepository.getInstance().save(user);
        System.out.println("before");
        System.out.println(FileUserRepository.getInstance().findById(user.getId()).get().getEmail());
        System.out.println("after");

        JCFUserRepository.getInstance().save(user);
        System.out.println("before2");
        System.out.println(FileUserRepository.getInstance().findById(user.getId()).get().getEmail());
        System.out.println("after2");


//        String uuid = "2cea3c15-77af-47f3-916c-3629aca93704";
//        BasicUserService.getInstance().delete(UUID.fromString(uuid));



    }


}
