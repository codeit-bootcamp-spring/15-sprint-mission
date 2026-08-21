package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        
        // JCF 구현 부분 (S)
/*        JCFUserService userService = new JCFUserService();
        channelService channelService = new channelService(userService);
        messageService messageService = new messageService(channelService,userService);*/
        
        // File 구현 부분 (S)
        FileUserService userService = new FileUserService();
        FileChannelService channelService = new FileChannelService(userService);
        FileMessageService messageService = new FileMessageService(channelService,userService);

        // 유저생성
        User user01 = userService.createUser("userNickName01");
        System.out.println("<< 최초 생성한 유저1 정보 >> \n" + user01 + "\n");

        // 유저1 닉네임 변경 후 출력
        userService.updateUser(user01.getId(),"userChangeNickName01");
        System.out.println("<< 최초 생성한 유저1 닉네임 변경 후 >>\n"+userService.getUser(user01.getId()) + "\n");

        // 유저2, 3, 4 추가 후 전체 출력
        User user02 = userService.createUser("userNickName02");
        User user03 = userService.createUser("userNickName03");
        User user04 = userService.createUser("userNickName04");
        List<User> allUser01 = userService.getUserAll();
        System.out.println("<< 유저2, 유저3 추가 후 유저 전체 출력 >>");
        allUser01.forEach(System.out::println);
        System.out.println();

        userService.userDelete(user03.getId());
        System.out.println("<< 유저3 삭제 후 유저 전체 출력 >>");
        List<User> allUser02 = userService.getUserAll();
        allUser02.forEach(System.out::println);
        System.out.println();

        Channel channel01 = channelService.createChannel("channel01nickName");
        Channel channel02 = channelService.createChannel("channel02nickName");
        System.out.println("<< 최초 생성 Channel01,02 정보 출력 >> \n" + channel01.getChannelInfo());
        System.out.println(channel02.getChannelInfo()+"\n");

        channelService.updateChannelName(channel01.getId(),"channel01ChangeNickName");
        System.out.println("<< Channel01 채널명 변경 후 출력 >> \n" + channelService.getChannelInfo(channel01.getId()).getChannelInfo() + "\n");

        List<Channel> channelList = channelService.getAllChannel();
        System.out.println("<< 채널 전체 호출 >>");
        channelList.forEach( (x) -> System.out.println(x.getChannelInfo()));
        System.out.println();

        channelService.deleteChannel(channel02.getId());
        List<Channel> channelList1 = channelService.getAllChannel();
        System.out.println("<< 채널2 삭제 후 전체 호출 >>");
        channelList1.forEach( (x) -> System.out.println(x.getChannelInfo()));
        System.out.println();

        channelService.addUserToChannel(channel01.getId(),user01.getId());
        channelService.addUserToChannel(channel01.getId(),user02.getId());
        channelService.addUserToChannel(channel01.getId(),user04.getId());
        List<User> channel1InUsers = channelService.getUserInChannel(channel01.getId());
        System.out.println("<< 채널1에 유저1,2 등록 후 채널1에 유저 리스트 호출 >> ");
        channel1InUsers.forEach(System.out::println);
        System.out.println();

        channelService.deleteUserInChannel(channel01.getId(),user04.getId());
        List<User> channel1InUsers1 = channelService.getUserInChannel(channel01.getId());
        System.out.println("<< 채널1에 유저4 삭제 후 채널1에 유저 리스트 호출 >> ");
        channel1InUsers1.forEach(System.out::println);
        System.out.println();

        Message user01Message1 = messageService.createMessage(channel01.getId(),user01.getId(),"How are you?");
        Message user01Message2 = messageService.createMessage(channel01.getId(),user02.getId(),"i'm fine!");
        Message user01Message3 = messageService.createMessage(channel01.getId(),user01.getId(),"Ok bye~");
        List<Message> channel1MessageAll = messageService.getMessagesByChannel(channel01.getId());
        System.out.println("<< 채널1 전체 메세지 출력 >>");
        channel1MessageAll.forEach(x -> {
            System.out.println(userService.getUserNickname(x.getAuthorId()) + " : " + x.getContents());
        });
        System.out.println();

        List<Message> user1MessageAll = messageService.getMessagesByUser(user01.getId());
        System.out.println("<< 유저1 전체 메세지 출력 >>");
        user1MessageAll.forEach(x -> {
            System.out.println(x.getContents());
        });
        System.out.println();

        messageService.updateMessageContents(user01Message3.getId(),"NONONO");
        String updatedContents = messageService.getMessagesByUser(user01.getId()).stream()
                .filter(m -> m.getId().equals(user01Message3.getId()))
                .findFirst()
                .map(Message::getContents)
                .orElse("");
        System.out.println("<< 유저1 마지막 메세지 변경 >>\n"+updatedContents+"\n");

        messageService.deleteMessage(user01Message1.getId());

        List<Message> channel1MessageAll1 = messageService.getMessagesByChannel(channel01.getId());
        System.out.println("<< 첫번째 메세지 삭제 후 전체 메세지 호출 >>");
        channel1MessageAll1.forEach(x -> {
            System.out.println(userService.getUserNickname(x.getAuthorId()) + " : " + x.getContents());
        });
    }
}