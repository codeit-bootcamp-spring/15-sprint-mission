package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository);


        System.out.println("=== User CRUD ===");

        // 등록
        User user1 = userService.create("민영", "minyeong@naver.com", "1234a");
        User user2 = userService.create("탱이", "taeng@naver.com", "5678a");
        System.out.println("등록: " + user1.getUserName() + ", " + user2.getUserName());

        // 단건 조회
        User foundUser = userService.read(user1.getId());
        System.out.println("단건 조회: " + foundUser.getUserName());

        // 다건 조회
        System.out.println("다건 조회:");
        for (User user : userService.readAll()) {
            System.out.println("- " + user.getUserName());
        }

        // 수정
        userService.update(user1.getId(), "민영(수정됨)", null, null);
        System.out.println("수정 완료");

        // 수정된 데이터 조회
        System.out.println("수정된 데이터 조회: " + userService.read(user1.getId()).getUserName());

        // 삭제
        userService.delete(user2.getId());
        System.out.println("삭제 완료");

        // 조회를 통해 삭제되었는지 확인
        System.out.println("삭제 후 조회:");
        for (User user : userService.readAll()) {
            System.out.println("- " + user.getUserName());
        }

        System.out.println("=== Channel CRUD ===");

        // 등록
        Channel channel1 = channelService.create("공부");
        Channel channel2 = channelService.create("수다");
        Channel channel3 = channelService.create("여행");
        System.out.println("등록: " + channel1.getChannelName() + ", " + channel2.getChannelName() + ", " + channel3.getChannelName());

        // 단건 조회
        System.out.println("단건 조회: " + channelService.read(channel1.getId()).getChannelName());

        // 다건 조회
        System.out.println("다건 조회:");
        for (Channel channel : channelService.readAll()) {
            System.out.println("- " + channel.getChannelName());
        }

        // 수정
        channelService.update(channel1.getId(), "공부(수정됨)");
        System.out.println("수정 완료");

        // 수정된 데이터 조회
        System.out.println("수정된 데이터 조회: " + channelService.read(channel1.getId()).getChannelName());

        // 삭제
        channelService.delete(channel2.getId());
        System.out.println("삭제 완료");

        // 조회를 통해 삭제되었는지 확인
        System.out.println("삭제 후 조회:");
        for (Channel channel : channelService.readAll()) {
            System.out.println("- " + channel.getChannelName());
        }

        System.out.println("=== Message CRUD ===");

        // 등록
        Message message1 = messageService.create("안녕하세요", user1.getId(), channel1.getId());
        Message message2 = messageService.create("환영합니다", user2.getId(), channel1.getId());
        System.out.println("등록: " + message1.getContents() + ", " + message2.getContents());

        // 단건 조회
        System.out.println("단건 조회: " + messageService.read(message1.getId()).getContents());

        // 다건 조회
        System.out.println("다건 조회:");
        for (Message message : messageService.readAll()) {
            System.out.println("- " + message.getContents());
        }

        // 수정
        messageService.update(message1.getId(), "안녕하세요(수정됨)");
        System.out.println("수정 완료");

        // 수정된 데이터 조회
        System.out.println("수정된 데이터 조회: " + messageService.read(message1.getId()).getContents());

        // 삭제
        messageService.delete(message1.getId());
        System.out.println("삭제 완료");

        // 조회를 통해 삭제되었는지 확인
        System.out.println("삭제 후 조회:");
        for (Message message : messageService.readAll()) {
            System.out.println("- " + message.getContents());
        }
    }
}