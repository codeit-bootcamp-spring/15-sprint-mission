package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }


    @Override
    public Message create(String mes, Channel channel, User user) {
        //일단 채널, 유저가 진짜 있는건지 확인하고.
        if (userService.read(user.getUser()) != null && channelService.read(channel.getChannelName()) != null) {
            Message message = new Message(mes, channel, user);
            if (messageRepository.create(message)) { //리포지토리에 저장하고.
                return message;
            }
        }
        else { //유저 ID, 채널 ID가 없으면,
            return null;
        }
        System.out.println("잘못된 데이터가 입력되었습니다.");
        return null;
    }

    @Override
    public List<Message> userReadAll(User user) {
        List<Message> messages = this.readAll();
        if (messages.isEmpty()) {
            System.out.println("현재 저장된 messages가 없습니다.");
            return null;
        }

        List<Message> result = new ArrayList<>();
        for (Message m: messages) {
            if (m.getUser().getUser().equals(user.getUser())) {
                result.add(m);
            }
        }
        return result; //없으면 빈 배열. null아님!
    }

    @Override
    public List<Message> channelReadAll(Channel channel) {
        List<Message> messages = this.readAll();
        if (messages.isEmpty()) {
            System.out.println("현재 저장된 messages가 없습니다.");
            return null;
        }

        List<Message> result = new ArrayList<>();
        for (Message m: messages) {
            if (m.getChannel().getChannelName().equals(channel.getChannelName())) {
                result.add(m);
            }
        }
        return result; //없으면 빈 배열. null아님!
    }

    @Override
    public Message read(UUID channelId, UUID userId) {
        List<Message> messages = this.readAll();
        if (messages.isEmpty()) {
            System.out.println("현재 저장된 messages가 없습니다.");
            return null;
        }

        List<Message> result = new ArrayList<>();
        int index;
        int count = 1;

        for (Message m: messages) {
            if (m.getChannel().getId().equals(channelId) && m.getUser().getId().equals(userId)) {
                System.out.println("[" + count++ + "] " + m.getUser().getUserId() + ": " + m.getMessage());
                result.add(m);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("읽어 올 메세지의 번호를 입력하세요: ");
        index = sc.nextInt();

        if (index <= 0 || index > result.size()) {
            System.out.println("잘못된 번호를 입력 했습니다.");
            return null;
        }

        return result.get(index - 1);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.readAll();
    }

    @Override
    public void update(Message message, String mes) {
        if (message == null) {
            System.out.println("저장소에서 해당 메시지를 찾을 수 없습니다.");
            return;
        }
        message.setMessage(mes);
        message.autoSetUpdatedAt();

        if (messageRepository.update(message)) {
            System.out.println("정상적으로 메세지 업데이트가 되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지 업데이트가 되지 않았습니다.");
        }
    }

    @Override
    public void delete(Message message) {
        if (messageRepository.delete(message)) {
            System.out.println("정상적으로 메세지가 삭제되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지가 삭제되지 않았습니다.");
        }
    }
}
