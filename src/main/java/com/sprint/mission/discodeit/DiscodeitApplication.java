package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) throws IOException {

		ConfigurableApplicationContext context =
				SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User user = userService.create("길춘배",
				"cuhbae11@naver.com",
				"test123");

		System.out.println("생성된 사용자:" + user.getName());
		System.out.println("사용자 ID : " + user.getId());
		System.out.println("전체 사용자:" + userService.findAll());

		Channel channel = new Channel(
				ChannelType.PUBLIC,
				"공지사항",
				"공지채널입니다.");

		System.out.println("생성된 채널: " + channel.getName());
		System.out.println("채널 ID: " + channel.getId());






	}
}