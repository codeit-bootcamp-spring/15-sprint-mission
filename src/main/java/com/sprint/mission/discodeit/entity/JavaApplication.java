package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.ServiceFactory;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {

        ServiceFactory serviceFactory = new ServiceFactory();

        ChannelService ch = serviceFactory.getChannelService();
        MessageService m = serviceFactory.getMessageService();
        UserService u = serviceFactory.getUserService();

        List<UUID> arrayList = new ArrayList<UUID>();

        // 채널 CRUD
        System.out.println("<< 채널 생성 >>");
        ch.createChannel("양띵", "마크");
        ch.createChannel("블루위키", "마크");

        System.out.println("<< 단건 조회 >> ");
        List<Channel> channels = ch.readAll();
        System.out.println("ID: "+ channels.get(0).getId() + " 채널명: "+ channels.get(0).getChannelName() + " 주제: "+ channels.get(0).getTopic());

        System.out.println("<< 다건 조회 >>");
        for (Channel channel : ch.readAll()) {
            System.out.println("ID: "+ channel.getId() + " 채널명: "+ channel.getChannelName() + " 주제: "+ channel.getTopic());
        }

        System.out.println("<< 수정 >> ");
        Channel result = ch.update(ch.readAll().get(0).getId(), "츄", "뷰티");

        System.out.println("<< 수정된 데이터 조회 >> ");
        System.out.println("ID: "+ result.getId() + " 채널명: "+ result.getChannelName() + " 주제: "+ result.getTopic());

        System.out.println("<< index 0 삭제 >> ");
        ch.deletebyID(ch.readAll().get(0).getId());

        System.out.println("<< 삭제 후 조회 >>");
        for (Channel channel : ch.readAll()) {
            System.out.println("ID: "+ channel.getId() + " 채널명: "+ channel.getChannelName() + " 주제: "+ channel.getTopic());
        }
        System.out.println();

        // 유저 CRUD
        System.out.println("<< 유저 생성 >>");
        u.createUser("최태민", "0108059765");
        u.createUser("진현선", "0107045043");
        u.createUser("김진모", "0102239994");

        System.out.println("<< 단건 조회 >>");
        List<User> users = u.readAll();
        System.out.println("ID: "+ users.get(0).getId() + " 휴대폰 번호: "+ users.get(0).getPhoneNum());

        System.out.println("<< 다건 조회 >>");
        for (User user : u.readAll()) {
            System.out.println("ID: "+ user.getId()+ " 이름: " + user.getName() + " 휴대폰 번호: "+ user.getPhoneNum());;
        }

        System.out.println("<< 수정 >>");
        User result3 = u.update(u.readAll().get(0).getId(), "최찬민", "0108720434");

        System.out.println("<< 수정된 데이터 조회 >>");
        System.out.println("ID: " + result3.getId()+ " 이름: " + result3.getName()+ " 휴대폰 번호: " + result3.getPhoneNum());

        System.out.println("<< index 1 삭제 >>");
        u.deleteById(u.readAll().get(1).getId());

        System.out.println("<< 삭제 후 조회 >>");
        for (User user : u.readAll()) {
            System.out.println("ID: " + user.getId() + " 이름: " + user.getName() + " 휴대폰 번호: " + user.getPhoneNum());
        }

        // 메시지 CRUD
        System.out.println();
        System.out.println("<< 메시지 생성 >>");
        m.createMessage("코드잇 스프린트는 정말 유익해", u.readAll().get(0), ch.readAll().get(0));
        m.createMessage("배고파", u.readAll().get(1), ch.readAll().get(0));


        System.out.println("<< 단건 조회 >>");
        List<Message> messages = m.readAll();
        System.out.println("ID: " + messages.get(0).getId() + " 메시지 내용: " + messages.get(0).getContent() +  " 유저 ID: "+ messages.get(0).getUserID() + " 채널 ID: " + messages.get(0).getChannelID());

        System.out.println("<< 다건 조회 >>");
        for (Message message : m.readAll()) {
            System.out.println("ID: " + message.getId() + " 메시지 내용: " + message.getContent() +  " 유저 ID: "+ message.getUserID() + " 채널 ID: " + message.getChannelID());
        }

        System.out.println("<< 수정 >>");
        Message result2 = m.update(m.readAll().get(0).getId(), "코드잇 스프린트는 재밌어");

        System.out.println("<< 수정된 데이터 조회 >> ");
        System.out.println("ID: " + result2.getId() + " 메시지 내용: " + result2.getContent() + " 유저 ID: " + result2.getUserID() + " 채널 ID: " + result2.getChannelID());

        System.out.println("<< index 0 삭제 >>");
        m.deleteByID(m.readAll().get(0).getId());

        System.out.println("<< 삭제 후 조회 >>");

        for (Message message : m.readAll()) {
            System.out.println("ID: " + message.getId() + " 메시지 내용: " + message.getContent() + " 유저 ID: " + message.getUserID() + " 채널 ID: " + message.getChannelID());
        }



    }
}
