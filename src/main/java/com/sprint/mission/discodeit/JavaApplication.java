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
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("========== JCF Repository 기반 테스트 ==========\n");
        runScenario(new JCFUserRepository(), new JCFChannelRepository(), new JCFMessageRepository());

        System.out.println("\n========== File Repository 기반 테스트 ==========\n");
        runScenario(new FileUserRepository(), new FileChannelRepository(), new FileMessageRepository());
    }

    static void runScenario(UserRepository userRepository,ChannelRepository channelRepository,MessageRepository messageRepository) {

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository, userRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

        // 유저 생성
        User user01 = userService.createUser("userNickName01");
        System.out.println("<< 최초 생성한 유저1 정보 >> \n" + user01 + "\n");

        // 유저1 닉네임 변경 후 출력
        userService.updateUser(user01.getId(), "userChangeNickName01");
        System.out.println("<< 최초 생성한 유저1 닉네임 변경 후 >>\n" + userService.getUser(user01.getId()) + "\n");

        // 유저2, 3, 4 추가 후 전체 출력
        User user02 = userService.createUser("userNickName02");
        User user03 = userService.createUser("userNickName03");
        User user04 = userService.createUser("userNickName04");
        List<User> allUser01 = userService.getUserAll();
        System.out.println("<< 유저2, 3, 4 추가 후 유저 전체 출력 >>");
        allUser01.forEach(System.out::println);
        System.out.println();

        // 유저3 삭제 후 전체 출력 (유저1, 유저2, 유저4)
        userService.userDelete(user03.getId());
        System.out.println("<< 유저3 삭제 후 유저 전체 출력 >>");
        userService.getUserAll().forEach(System.out::println);
        System.out.println();

        // 채널1, 2 생성
        Channel channel01 = channelService.createChannel("channel01nickName");
        Channel channel02 = channelService.createChannel("channel02nickName");
        System.out.println("<< 최초 생성 Channel01, 02 정보 출력 >> \n" + channel01.getChannelInfo());
        System.out.println(channel02.getChannelInfo() + "\n");

        // 채널1 채널명 변경
        channelService.updateChannelName(channel01.getId(), "channel01ChangeNickName");
        System.out.println("<< Channel01 채널명 변경 후 출력 >> \n" + channelService.getChannelInfo(channel01.getId()) + "\n");

        // 채널 전체 출력
        System.out.println("<< 채널 전체 호출 >>");
        channelService.getAllChannel().forEach(x -> System.out.println(x.getChannelInfo()));
        System.out.println();

        // 채널2 삭제 후 전체 호출
        channelService.deleteChannel(channel02.getId());
        System.out.println("<< 채널2 삭제 후 전체 호출 >>"); // 채널1만 남음
        channelService.getAllChannel().forEach(x -> System.out.println(x.getChannelInfo()));
        System.out.println();

        // 채널1에 유저1, 2, 4 등록
        channelService.addUserToChannel(channel01.getId(), user01.getId());
        channelService.addUserToChannel(channel01.getId(), user02.getId());
        channelService.addUserToChannel(channel01.getId(), user04.getId());
        System.out.println("<< 채널1에 유저1,2,4 등록 후 채널1에 유저 리스트 호출 >> ");
        channelService.getUserInChannel(channel01.getId()).forEach(System.out::println);
        System.out.println();

        // 채널1에 포함된 유저4 삭제
        channelService.deleteUserInChannel(channel01.getId(), user04.getId());
        System.out.println("<< 채널1에 유저4 삭제 후 채널1에 유저 리스트 호출 >> ");
        channelService.getUserInChannel(channel01.getId()).forEach(System.out::println); // 유저 1,2
        System.out.println();

        // 채널1에서 유저1,2 메세지 보내기 및 채널1 메세지 전체 호출
        Message user01Message1 = messageService.createMessage(channel01.getId(), user01.getId(), "How are you?");
        messageService.createMessage(channel01.getId(), user02.getId(), "i'm fine!");
        Message user01Message3 = messageService.createMessage(channel01.getId(), user01.getId(), "Ok bye~");
        System.out.println("<< 채널1 전체 메세지 출력 >>");
        messageService.getMessagesByChannel(channel01.getId()).forEach(x ->
                System.out.println(userService.getUserNickname(x.getAuthorId()) + " : " + x.getContents()));
        System.out.println();

        // 유저1이 보낸 전체 메세지 호출
        System.out.println("<< 유저1 전체 메세지 출력 >>");
        messageService.getMessagesByUser(user01.getId()).forEach(x -> System.out.println(x.getContents()));
        System.out.println();

        // 메세지 내용 변경
        messageService.updateMessageContents(user01Message3.getId(), "NONONO");
        System.out.println("<< 유저1 마지막 메세지 변경 >>\n" +
                messageService.getMessagesByUser(user01.getId()).stream()
                        .filter(m -> m.getId().equals(user01Message3.getId()))
                        .findFirst().map(Message::getContents).orElse("") + "\n");

        // 메세지 삭제
        messageService.deleteMessage(user01Message1.getId());

        // 다시 전체 메세지 호출
        System.out.println("<< 첫번째 메세지 삭제 후 전체 메세지 호출 >>");
        messageService.getMessagesByChannel(channel01.getId()).forEach(x ->
                System.out.println(userService.getUserNickname(x.getAuthorId()) + " : " + x.getContents()));

        // 연관 도메인 검증 테스트: 존재하지 않는 채널/유저로 메세지 생성 시 예외 발생 확인
        try {
            messageService.createMessage(java.util.UUID.randomUUID(), user01.getId(), "존재하지 않는 채널 테스트");
        } catch (IllegalArgumentException e) {
            System.out.println("\n<< 존재하지 않는 채널로 메세지 생성 시 예외 발생 확인 >>\n" + e.getMessage());
        }
    }
}
