package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) throws IOException {
		ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		AuthService authService = context.getBean(AuthService.class);
		UserStatusService statusService = context.getBean(UserStatusService.class);
		ReadStatusService readService = context.getBean(ReadStatusService.class);

		UserCreateRequest userRequestDTO1 = new UserCreateRequest("userNickName01","user01","user01@naver.com","user01password");
		byte[] photo = Files.readAllBytes(Path.of("examples/profile.png"));
		BinaryContentCreateRequest profile = new BinaryContentCreateRequest("profile.png", "image/png", photo);

		UserResponse user01 = userService.create(userRequestDTO1, profile);

		UserUpdateRequest userUpdateDTO = new UserUpdateRequest(user01.id(), "userChangeNickName01","userChange01","userChangeEmail@naver.com","user01ChangePassword");
		user01 = userService.update(userUpdateDTO,null);
		System.out.println("<< 최초 생성한 유저1 닉네임 변경 후 >>\n" + userService.find(user01.id()).toString() + "\n");

		UserCreateRequest userRequestDTO2 = new UserCreateRequest("userNickName02","user02","user02password","user02@naver.com");
		UserResponse user02 = userService.create(userRequestDTO2,null);
		UserCreateRequest userRequestDTO3 = new UserCreateRequest("userNickName03","user03","user03password","user03@naver.com");
		UserResponse user03 = userService.create(userRequestDTO3,null);

		// 모든 사용자의 접속 상태 조회
		System.out.println("<< 전체 유저 접속 상태 >>");
		statusService.findAll().forEach(status -> {
			System.out.println("접속 상태 ID: " + status.getId());
			System.out.println("사용자 ID: " + status.getUserId());
			System.out.println("마지막 접속 시각: " + status.getLastActiveAt());
			System.out.println("생성 시각: " + status.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")));
			System.out.println("수정 시각: " + status.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")));
			System.out.println("현재 접속 중: " + status.isOnline());
			System.out.println();
		});

		List<UserResponse> allUser01 = userService.findAll();
		allUser01.forEach(System.out::println);
		System.out.println();

		userService.delete(user02.id());
		userService.findAll().forEach(System.out::println);
		System.out.println();
		UserResponse loggedIn = authService.login(new LoginRequest(user01.username(), "user01ChangePassword"));
		System.out.println("로그인 성공: userId=" + loggedIn.id());

		// 채널1, 2 생성
		ChannelResponse publicChannel = channelService.createPublic(new PublicChannelCreateRequest("publicChannel", "channelDST"));
		ChannelResponse privateChannel = channelService.createPrivate(new PrivateChannelCreateRequest(List.of(user01.id(),user03.id())));

		// 채널1 채널명 변경
		ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(publicChannel.getId(),"publicChannelUpdate","channelDSTUpdate");
		channelService.update(channelUpdateRequest);
		System.out.println("<< Channel01 채널명 변경 후 출력 >> \n" + channelService.find(publicChannel.getId()) + "\n");

		// 채널 전체 출력
		System.out.println("<< PUBLIC 채널 전체 호출 >>");
		channelService.findAllPublic().forEach(x -> System.out.println(x.toString()));
		System.out.println();

		ChannelResponse publicChannel2 = channelService.createPublic(new PublicChannelCreateRequest("publicChannel", "channelDST"));
		channelService.delete(publicChannel2.getId());
		System.out.println();

		channelService.addUserToChannel(publicChannel.getId(), user01.id());
		channelService.addUserToChannel(publicChannel.getId(), user03.id());
		channelService.findAllByUserId(user01.id()).forEach(x -> System.out.println(x.toString()));

		MessageCreateRequest messageCreateRequest1 = new MessageCreateRequest(publicChannel.getId(), user01.id(), "How are you?");
		Message message1 = messageService.create(messageCreateRequest1,null);

		MessageCreateRequest messageCreateRequest2 = new MessageCreateRequest(publicChannel.getId(), user03.id(), "I'm Fine");
		Message message2 = messageService.create(messageCreateRequest2,null);

		MessageCreateRequest messageCreateRequest3 = new MessageCreateRequest(publicChannel.getId(), user01.id(), "Ok bye~");
		Message message3 = messageService.create(messageCreateRequest3,null);

		System.out.println("<< 채널1 전체 메세지 출력 >>");
		messageService.findAllByChannelId(publicChannel.getId())
				.forEach(message ->
						System.out.println(userService.find(message.getAuthorId()).nickname()+ " : "+ message.getContent()));
		System.out.println();

		MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(message3.getId(),"NONONO");
		messageService.update(messageUpdateRequest);

		messageService.delete(message3.getId());
	}
}
