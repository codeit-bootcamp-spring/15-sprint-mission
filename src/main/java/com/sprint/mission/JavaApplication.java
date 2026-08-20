package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        User user;
        User user1;

        JCFUserService users = new JCFUserService();
        ChannelService channels = new JCFChannelService();
        MessageService messages = new JCFMessageService(users, channels);


        user = new User("seok0630");
        user1 = new User("shon0430");

        users.create(user);
        users.create(user1);

        Channel channel = new Channel("1");
        Channel channel1 = new Channel("2");
        channels.create(channel);
        channels.create(channel1);


        Message message = new Message("안녕하세용", user, channel);
        Message message1 = new Message("안녕하삼", user1, channel);


        messages.create(message);
        messages.create(message1);


        System.out.println("↓↓↓↓↓↓↓↓모든 유저 출력↓↓↓↓↓↓↓↓");
        users.readAll().stream().forEach(x-> System.out.print(x.getUser() + " "));
        System.out.println();
        System.out.println();

        System.out.println("↓↓↓↓↓↓↓↓모든 채널 출력↓↓↓↓↓↓↓↓");
        channels.readAll().stream().forEach(x-> System.out.print(x.getChannel() + " "));
        System.out.println();
        System.out.println();

        System.out.println("↓↓↓↓↓↓↓↓모든 메시지 출력↓↓↓↓↓↓↓↓");
        //messages.update(message, "하이요");
        messages.readAll().stream().forEach(x-> System.out.println(x.getMessage() + " "));
        System.out.println();
        System.out.println();

        System.out.print("단일 유저 출력: " + users.read(user).getUser());
        System.out.println();
        System.out.print("단일 채널 출력: " + channels.read(channel).getChannel());
        System.out.println();
        System.out.print("단일 메시지 출력: " + messages.read(message).getMessage());
        System.out.println();

        users.update(user, "tjrrb0630");
        channels.update(channel, "공지 채널");
        messages.update(message, "HI!");

        System.out.print("[업데이트 후] 단일 유저 출력: " + users.read(user).getUser());
        System.out.println();
        System.out.print("[업데이트 후] 단일 채널 출력: " + channels.read(channel).getChannel());
        System.out.println();
        System.out.print("[업데이트 후] 단일 메시지 출력: " + messages.read(message).getMessage());
        System.out.println();

        users.delete(user);
        channels.delete(channel);
        messages.delete(message);

        messages.create(message);
    }

    /* 1. 첫 실행시에는 User 객체의 UUID를 가지고 있으니까 자유롭게 delete가 가능한데, 두번째 실행부터는 User user객체를 동일하게
    만들더라도, UUID가 달라진다. 그래서 UUID로 객체를 비교하여 사용자가 원하는 객체를 가져오거나(read()) 하는 것이 어렵다.
    그렇다고 delete나 update와 같은 함수에 UUID가 아니라 각각의 entity 객체가 가지는 멤버 변수로 비교하여 원하는 값을 가져오게 한다면
    기능 구현이 가능할 것 같지만 UUID 자체가 객체를 식별하기 위한 필드 아니었나? 의미가 사라지는 느낌이다.
     */

}
