package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService) {
		UserCreateRequest request =
				new UserCreateRequest("woody@codeit.com", "woody1234", "woody", NitroLevel.BASIC, null);
		User user = userService.create(request);
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		Channel channel = channelService.create("공지", ChannelType.PUBLIC);
		return channel;
	}

	static void messageCreateTest(MessageService messageService , Channel channel, User user) {
		Message message = messageService.create(channel.getId(), user.getId(),"안녕하세요.");
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
