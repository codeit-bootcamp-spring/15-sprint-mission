package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
// Repository 인터페이스 및 구현체 imports
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.jcfChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.jcfMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.jcfUserRepository;
// Service 인터페이스 및 구현체 imports
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    static User setupUser(UserService userService) {
        // 실제 User 생성자 규격에 맞춰 (username, email)만 전달
        User user = userService.create("woody", "woody@codeit.com");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        // 실제 Channel 생성자 규격에 맞춰 name만 전달
        Channel channel = channelService.create("공지");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // ========================================================
        // [테스트 1] JCF*Repository 구현체를 활용한 테스트
        // ========================================================
        System.out.println("=== [JCF Repository 테스트] ===");
        UserRepository jcfUserRepository = new jcfUserRepository();
        ChannelRepository jcfChannelRepository = new jcfChannelRepository();
        MessageRepository jcfMessageRepository = new jcfMessageRepository();

        UserService userService = new BasicUserService(jcfUserRepository);
        ChannelService channelService = new BasicChannelService(jcfChannelRepository);
        MessageService messageService = new BasicMessageService(jcfMessageRepository, jcfUserRepository, jcfChannelRepository);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);


        // ========================================================
        // [테스트 2] File*Repository 구현체를 활용한 테스트
        // ========================================================
        System.out.println("\n=== [File Repository 테스트] ===");
        UserRepository fileUserRepository = new FileUserRepository();
        ChannelRepository fileChannelRepository = new FileChannelRepository();
        MessageRepository fileMessageRepository = new FileMessageRepository();

        UserService fileUserService = new BasicUserService(fileUserRepository);
        ChannelService fileChannelService = new BasicChannelService(fileChannelRepository);
        MessageService fileMessageService = new BasicMessageService(fileMessageRepository, fileUserRepository, fileChannelRepository);

        User fileUser = setupUser(fileUserService);
        Channel fileChannel = setupChannel(fileChannelService);
        messageCreateTest(fileMessageService, fileChannel, fileUser);
    }
}


//package com.sprint.mission.discodeit;

//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;

//import com.sprint.mission.discodeit.service.file.FileChannelService;
//import com.sprint.mission.discodeit.service.file.FileMessageService;
//import com.sprint.mission.discodeit.service.file.FileUserService;

//import java.util.List;
//import java.util.UUID;

//public class JavaApplication {
   // public static void main(String[] args) {


     //   UserService userService = new FileUserService();
       // ChannelService channelService = new FileChannelService();
        //MessageService messageService = new FileMessageService(userService, channelService);

        //System.out.println("===== 1. 등록 =====" );
        //User user1 = userService.create("김코드", "codeit@example.com");
    //    User user2 = userService.create("이디스", "discode@example.com");
      //  Channel channel1 = channelService.create("일반-채널");

        //System.out.println("생선된 우저 1 ID : " + user1.getId());
        //System.out.println("생성된 채널 ID : " + channel1.getId());

        //Message message1 = messageService.create("안녕하세요!", channel1.getId(), user1.getId());
        //System.out.println("생성된 메시지 내용 : " + message1.getContent());
//
  //      System.out.println("\n===== 2. 조회 (Find - 단건 $ 다건) =====");
    //    User foundUser = userService.find(user1.getId());
      //  System.out.println("[단건 조회] 유저 이름 : " + foundUser.getUsername());

        //List<User> allUsers = userService.findAll();
 //       System.out.println("[다건 조회] 전체 유저 수 : " + allUsers.size());
//
  //      System.out.println("\n===== 3. 수정(Update) 및 수정된 데이터 조회 =====");
    //    userService.update(user1.getId(), " 김코드_수정", "updated_codeit@ecample.com");
      //  User updatedUser = userService.find(user1.getId());
        //System.out.println("[수정 후 조회] 변경된 이름 : " + updatedUser.getUsername());
 //       System.out.println("[수정 후 조회] 수정 시간(updatedAt): " + updatedUser.getUpdatedAt());

   //     System.out.println("\n===== 4. 삭제(Delete) 및 삭제 확인 ===== ");
     //   userService.delete(user2.getId());
       // User deletedUser = userService.find(user2.getId());
        //System.out.println("[삭제 후 조회] 유저2 존재 여부 (null 이면 성공): " + deletedUser);

        //System.out.println("\n===== 5. 심화 요구사항 테스트 (예외 검증) =====");
        //try {
          //  messageService.create("에러 발생 메시지", channel1.getId(), UUID.randomUUID());
        //} catch (IllegalArgumentException e) {
          //  System.out.println("예외 발생 확인 성공 : " + e.getMessage());
        //}

    //}
//}
