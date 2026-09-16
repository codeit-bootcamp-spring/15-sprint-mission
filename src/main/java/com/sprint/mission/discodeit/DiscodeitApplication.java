package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService) {
		UserCreateRequest request =
				new UserCreateRequest("woody@codeit.com", "woody1234", "woody", NitroLevel.BASIC, null);
		User user = userService.create(request);
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지");
		Channel channel = channelService.create(request);
		return channel;
	}

	static void messageCreateTest(MessageService messageService , Channel channel, User user) {
		MessageCreateRequest mcr= new MessageCreateRequest( channel.getId(), user.getId(),"안녕하세요.",null);
		Message message = messageService.create(mcr);
		System.out.println("메시지 생성: " + message.getId());
	}


	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		// 테스트
		messageCreateTest(messageService, channel, user);

		System.out.println("끝");
	}

}
