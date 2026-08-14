package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFUserService;

import java.util.*;

public class JavaApplicaton {
    public static void main(String[] args) {
        JCFChannelService ch = new JCFChannelService();
        JCFMessageService m = new JCFMessageService();
        JCFUserService u = new JCFUserService();

        List<UUID> arrayList = new ArrayList<UUID>();

        System.out.println("<< 생성 >>");
        ch.createChannel("양띵", "마크");
        ch.createChannel("블루위키", "마크");

        System.out.println("<< 다건 조회 >>");
        for (Channel channel : ch.readAll()) {
            arrayList.add(channel.getId());
            System.out.println("ID: "+ channel.getId() + " 채널명: "+ channel.getChannelName() + " 주제: "+ channel.getTopic());
        }

        System.out.println("<< 단건 조회 >> ");
        System.out.println("ID: "+ ch.getById(arrayList.get(0)).getId() + " 채널명: "+ ch.getById(arrayList.get(0)).getChannelName() + " 주제: "+ ch.getById(arrayList.get(0)).getTopic());

        System.out.println("<< 수정 >> ");
        Channel result = ch.update(arrayList.get(0), "츄", "뷰티");

        System.out.println("<< 수정된 데이터 조회 >> ");
        System.out.println("ID: "+ result.getId() + " 채널명: "+ result.getChannelName() + " 주제: "+ result.getTopic());

        System.out.println("<< 삭제 >> ");
        ch.deletebyID(arrayList.get(0));

        System.out.println("<< 삭제 후 조회 >>");
        for (Channel channel : ch.readAll()) {
            System.out.println("ID: "+ channel.getId() + " 채널명: "+ channel.getChannelName() + " 주제: "+ channel.getTopic());
        }


    }
}
