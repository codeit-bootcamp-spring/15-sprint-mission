package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        boolean running = true;
        Scanner sc = new Scanner(System.in);

        // Repository 생성
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // BasicService 생성 + Repository 주입
        UserService userService =
                new BasicUserService(userRepository);

        ChannelService channelService =
                new BasicChannelService(channelRepository);

        MessageService messageService =
                new BasicMessageService(messageRepository);

        while (running) {
            try {
                System.out.println("===== 짭스코드 =====");
                System.out.println("1. 로그인");
                System.out.println("2. 계정 만들기");
                System.out.println("0. 종료");


                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1: // 로그인

                        System.out.print("아이디(이메일): ");
                        String loginEmail = sc.nextLine();

                        System.out.print("비밀번호: ");
                        String loginPassword = sc.nextLine();

                        boolean loginSuccess = false;
                        User currentUser = null;

                        for (User user : userService.readAll()) {

                            if (user.getEmail().equals(loginEmail)
                                    && user.getPassword().equals(loginPassword)) {

                                loginSuccess = true;
                                currentUser = user;
                                System.out.println(user.getName() + "님 로그인 성공!");
                                break;
                            }
                        }
                        if (loginSuccess) {

                            boolean loggedIn = true;

                            while (loggedIn) {

                                try {
                                    System.out.println("===== 메인 메뉴 =====");
                                    System.out.println("1. 채널 만들기");
                                    System.out.println("2. 채널 목록");
                                    System.out.println("3. 채널명 수정");
                                    System.out.println("4. 메시지 작성");
                                    System.out.println("5. 메시지 목록");
                                    System.out.println("6. 메시지 수정");
                                    System.out.println("0. 로그아웃");

                                    int menuChoice = sc.nextInt();
                                    sc.nextLine();

                                    switch (menuChoice) {

                                        case 1: //채널생성
                                            System.out.print("채널 이름: ");
                                            String channelName = sc.nextLine();

                                            Channel channel =
                                                    channelService.create(channelName);

                                            System.out.println("채널 생성 완료");
                                            break;

                                        case 2: //채널 이름 확인
                                            for (Channel c : channelService.readAll()) {
                                                System.out.println(
                                                        c.getId() + " / " + c.getChannelName()
                                                );
                                            }
                                            break;

                                        case 3: //채널명 수정
                                            List<Channel> channels1 = channelService.readAll();

                                            for (int i = 0; i < channels1.size(); i++) {
                                                System.out.println(
                                                        (i + 1) + ". " + channels1.get(i).getChannelName()
                                                );
                                            }

                                            System.out.print("수정할 채널 선택 > ");
                                            System.out.println("만약 수정할 채널이 없다면\"0\"을 입력하세요.");
                                            int channelNumber1 = sc.nextInt();
                                            sc.nextLine();

                                            if (channelNumber1 == 0) {
                                                break;
                                            }

                                            Channel selectedChannel1 =
                                                    channels1.get(channelNumber1 - 1);

                                            System.out.print("새 채널명 > ");
                                            System.out.println("만약 채널을 잘못 선택 했다면\"0\"을 입력하세요.");
                                            String newChannelName = sc.nextLine();

                                            if (newChannelName.equals("0")) {
                                                break;
                                            }

                                            channelService.update(
                                                    selectedChannel1.getId(),
                                                    newChannelName
                                            );

                                            System.out.println("채널명 수정 완료");
                                            break;

                                        case 4: //메시지 작성
                                            List<Channel> channels = channelService.readAll();

                                            for (int i = 0; i < channels.size(); i++) {
                                                System.out.println((i + 1) + ". " + channels.get(i).getChannelName());
                                            }

                                            System.out.print("채널 선택 > ");
                                            System.out.println("만약 선택할 채널이 없다면\"0\"을 입력하세요.");
                                            int channelNumber = sc.nextInt();

                                            if (channelNumber == 0) {
                                                break;
                                            }

                                            sc.nextLine();
                                            Channel selectedChannel = channels.get(channelNumber - 1);

                                            UUID channelId = selectedChannel.getId();

                                            System.out.print("메시지 > ");
                                            System.out.println("만약 입력할 메시지가 없다면\"0\"을 입력하세요.");
                                            String content = sc.nextLine();

                                            if (content.equals("0")) {
                                                break;
                                            }

                                            messageService.create(channelId, currentUser.getId(), content);

                                            System.out.println("메시지 작성 완료");
                                            break;

                                        case 5: //메시지 내용들 모두 확인하기(최신 작성을 기준으로 보여줌)
                                            List<Message> messages =
                                                    new ArrayList<>(messageService.readAll());

                                            messages.sort((m1, m2) ->
                                                    Long.compare(m2.getCreatedAt(), m1.getCreatedAt())
                                            );

                                            DateTimeFormatter formatter =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                                            for (Message message : messages) {

                                                User writer = userService.read(message.getUserId());

                                                Channel messageChannel =
                                                        channelService.read(message.getChannelId());

                                                LocalDateTime time = LocalDateTime.ofInstant(
                                                        Instant.ofEpochMilli(message.getCreatedAt()),
                                                        ZoneId.systemDefault()
                                                );

                                                System.out.println(
                                                        "[" + messageChannel.getChannelName() + "] "
                                                                + "[" + time.format(formatter) + "] "
                                                                + writer.getName() + ": "
                                                                +message.getContent()
                                                );
                                            }
                                            break;

                                        case 6: //메시지 수정
                                            List<Message> messages1 = new ArrayList<>(messageService.readAll());

                                            for (int i = 0; i < messages1.size(); i++) {
                                                System.out.println((i + 1) + ". " + messages1.get(i).getContent());
                                            }

                                            System.out.print("수정할 메시지 선택 > ");
                                            System.out.println("만약 수정할 메시지가 없다면\"0\"을 입력하세요.");

                                            int message1Number = sc.nextInt();
                                            sc.nextLine();

                                            if (message1Number == 0) {
                                                break;
                                            }

                                            Message selectedMessage =
                                                    messages1.get(message1Number - 1);

                                            System.out.print("새 내용 > ");
                                            System.out.println("만약 수정할 메시지가 없다면\"0\"을 입력하세요.");
                                            String newContent = sc.nextLine();
                                            if (newContent.equals("0")) {
                                                break;
                                            }

                                            messageService.update(
                                                    selectedMessage.getId(),
                                                    newContent
                                            );

                                            System.out.println("메시지 수정 완료");
                                            break;

                                        case 0: //종료
                                            loggedIn = false;
                                            System.out.println("로그아웃되었습니다.");
                                            break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("올바른 메뉴를 선택해주세요.");
                                    System.out.println("오류 종류: " + e.getClass().getSimpleName());
                                    System.out.println("오류 내용: " + e.getMessage());
                                    sc.nextLine();  // 잘못 입력한 값을 버림
                                }
                            }
                        }

                        if (!loginSuccess) {
                            System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
                        }

                        break;


                    case 2: { // 계정 만들기 메뉴
                        System.out.print("이름: ");
                        String name = sc.nextLine();

                        System.out.println("이메일 형식만 입력이 가능합니다..");
                        System.out.print("이메일 (0: 뒤로가기): ");
                        String email = sc.nextLine();

                        if (email.equals("0")) {
                            break;
                        }

                        String emailRegex =
                                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

                        if (!email.matches(emailRegex)) {
                            System.out.println("올바른 이메일 형식이 아닙니다.");
                            break;
                        }

                        System.out.println("비밀번호는 8자 이상이며 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
                        System.out.print("비밀번호 (0: 뒤로가기): ");
                        String password = sc.nextLine();

                        if (password.equals("0")) {
                            break;
                        }

                        String passwordRegex =
                                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
                        if (!password.matches(passwordRegex)) {
                            System.out.println("비밀번호는 8자 이상이며 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
                            );
                            break;
                        }

                        userService.create(name, email, password);

                        System.out.println("계정이 생성되었습니다.");
                        break;
                    }

                    case 0: //프로그램 종료 메뉴
                        System.out.println("프로그램을 종료합니다");
                        running = false;
                        break;

                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택해주세요.");

                }
            } catch (Exception e2) {
                System.out.println("올바를 메뉴를 선택해주세요.");
                System.out.println("오류 종류: " + e2.getClass().getSimpleName());
                System.out.println("오류 내용: " + e2.getMessage());
                sc.nextLine();  // 잘못 입력한 값을 버림

            }
        }

    }
}