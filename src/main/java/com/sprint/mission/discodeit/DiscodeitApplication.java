package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {
/*    public static void main(String[] args) throws AccountException, InstanceNotFoundException {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        UserCreateRequest userRequest =
                new UserCreateRequest("woody", "woody@codeit.com", "woody1234");

        User user = userService.create(userRequest, null);

        // 3. PUBLIC 채널을 생성합니다.
        ChannelCreateRequest channelRequest =
                new ChannelCreateRequest("공지", "공지사항 채널입니다.");

        Channel channel = channelService.createPublicChannel(channelRequest);

        // 4. 생성한 채널과 사용자의 ID로 메시지를 작성합니다.
        MessageCreateRequest messageRequest =
                new MessageCreateRequest("반가워요.", channel.getId(), user.getId());

        Message message = messageService.create(messageRequest, List.of());
        System.out.println("생성한 메시지: " + message.getContent());

        // 5. 저장된 결과를 서비스로 다시 조회합니다.
        System.out.println(userService.find(user.getId()));
        System.out.println(channelService.find(channel.getId()));

        messageService.findAllByChannelId(channel.getId())
                .forEach(m -> System.out.println(m.getContent()));
    }*/

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
  }

}
