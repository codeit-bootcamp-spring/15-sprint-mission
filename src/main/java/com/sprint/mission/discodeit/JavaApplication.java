package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCF_ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCF_MessageService;
import com.sprint.mission.discodeit.service.jcf.JCF_UserService;

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
// 여기서 서비스 객체 생성 및 테스트 코드 작성
        boolean running = true;
        Scanner sc = new Scanner(System.in);
        JCF_UserService userService = new JCF_UserService();
        JCF_ChannelService channelService = new JCF_ChannelService();
        JCF_MessageService messageService = new JCF_MessageService();

        //테스트 메시지(차후에 지울것)
        Channel testChannel = channelService.readAll().get(0);
        User testUser = userService.readAll().get(0);
        messageService.create(
                testChannel.getId(),
                testUser.getId(),
                "테스트 메시지입니다."
        );
        messageService.create(
                testChannel.getId(),
                testUser.getId(),
                "두번째 테스트 메시지입니다. ##"
        );


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
                                            int channelNumber1 = sc.nextInt();
                                            sc.nextLine();

                                            Channel selectedChannel1 =
                                                    channels1.get(channelNumber1 - 1);

                                            System.out.print("새 채널명 > ");
                                            String newChannelName = sc.nextLine();

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
                                            int channelNumber = sc.nextInt();
                                            sc.nextLine();

                                            Channel selectedChannel = channels.get(channelNumber - 1);

                                            UUID channelId = selectedChannel.getId();

                                            System.out.print("메시지 > ");
                                            String content = sc.nextLine();

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
                                            int message1Number = sc.nextInt();
                                            sc.nextLine();

                                            Message selectedMessage =
                                                    messages1.get(message1Number - 1);

                                            System.out.print("새 내용 > ");
                                            String newContent = sc.nextLine();

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


                    case 2: // 계정 만들기 메뉴
                        System.out.print("이름: ");
                        String name = sc.nextLine();

                        System.out.print("이메일: ");
                        String email = sc.nextLine();

                        System.out.print("비밀번호: ");
                        String password = sc.nextLine();

                        userService.create(name, email, password);

                        System.out.println("계정이 생성되었습니다.");
                        break;

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