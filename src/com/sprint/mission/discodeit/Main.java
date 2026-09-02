package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // 1. File Repository 생성 (파일 IO 및 역직렬화 수행)
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

// 2. BasicService에 Repository 주입 (의존성 주입 DI)
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository);
        boolean running = true;

        while (running) {
            System.out.println("\n===== Discodeit 관리 콘솔 =====");
            System.out.println("1. 데이터 등록 (User / Channel / Message)");
            System.out.println("2. 전체 데이터 조회");
            System.out.println("3. 데이터 수정");
            System.out.println("4. 수정된 데이터 상세 조회");
            System.out.println("5. 데이터 삭제");
            System.out.println("6. 삭제 여부 확인");
            System.out.println("0. 종료");
            System.out.print("선택> ");

            int choice;


            try{
                choice = sc.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("숫자로 입력해주세요.");
                continue;
            }

            switch (choice) {
                // 1. 등록
                case 1 -> {
                    System.out.println("\n[1. 등록] 1) 유저  2) 채널  3) 메시지");
                    System.out.print("선택> ");
                    int type = sc.nextInt();
                    sc.nextLine();

                    if (type == 1) {
                        System.out.print("유저 이름: ");
                        String name = sc.nextLine();
                        System.out.print("이메일: ");
                        String email = sc.nextLine();
                        System.out.print("패스워드: ");
                        String password = sc.nextLine();
                        User u = userService.create(name, email,password);
                        System.out.println(" 등록 완료 [User ID: " + u.getId() + "]");
                    } else if (type == 2) {
                        System.out.print("채널 이름: ");
                        String name = sc.nextLine();
                        System.out.print("채널 설명: ");
                        String desc = sc.nextLine();
                        Channel c = channelService.create(name, desc);
                        System.out.println(" 등록 완료 [Channel ID: " + c.getId() + "]");
                    } else if (type == 3) {
                        try {
                            System.out.print("작성자(User) UUID: ");
                            UUID userId = UUID.fromString(sc.nextLine().trim());
                            System.out.print("채널(Channel) UUID: ");
                            UUID channelId = UUID.fromString(sc.nextLine().trim());
                            System.out.print("메시지 내용: ");
                            String content = sc.nextLine();

                            Message m = messageService.create(content, userId, channelId);
                            System.out.println(" 등록 완료 [Message ID: " + m.getId() + "]");
                        } catch (Exception e) {
                            System.out.println("등록 실패: " + e.getMessage());
                        }
                    } else {
                        System.out.println("잘못된 선택입니다.");
                    }
                }

                // 2. 조회 (단건, 다건)
                case 2 -> {
                    System.out.println("\n[2. 조회] 1) 다건 조회 (전체 목록)  2) 단건 조회 (ID 검색)");
                    System.out.print("선택> ");
                    int readType = sc.nextInt();
                    sc.nextLine();

                    if (readType == 1) {
                        System.out.println("--- [전체 유저 목록 (" + userService.readALL().size() + "명)] ---");
                        userService.readALL().forEach(u -> System.out.println(" - " + u.getUsername() + " (" + u.getEmail() + ") | ID: " + u.getId()));

                        System.out.println("--- [전체 채널 목록 (" + channelService.readAll().size() + "개)] ---");
                        channelService.readAll().forEach(c -> System.out.println(" - #" + c.getName() + " : " + c.getDescription() + " | ID: " + c.getId()));

                        System.out.println("--- [전체 메시지 목록 (" + messageService.readAll().size() + "건)] ---");
                        messageService.readAll().forEach(m -> System.out.println(" - " + m.getContent() + " | ID: " + m.getId()));
                    } else if (readType == 2) {
                        try {
                            System.out.print("조회할 UUID 입력: ");
                            UUID id = UUID.fromString(sc.nextLine().trim());

                            userService.read(id).ifPresent(u -> System.out.println("[User 발견] " + u.getUsername() + " (" + u.getEmail() + ")"));
                            channelService.read(id).ifPresent(c -> System.out.println("[Channel 발견] #" + c.getName() + " (" + c.getDescription() + ")"));
                            messageService.read(id).ifPresent(m -> System.out.println("[Message 발견] " + m.getContent()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("잘못된 UUID 형식입니다.");
                        }
                    }
                }

                // 3. 수정
                case 3 -> {
                    System.out.println("\n[3. 수정] 1) User 수정  2) Channel 수정  3) Message 수정");
                    System.out.print("선택> ");
                    int updateType = sc.nextInt();
                    sc.nextLine();

                    try {
                        System.out.print("수정할 UUID 입력: ");
                        UUID id = UUID.fromString(sc.nextLine().trim());

                        if (updateType == 1) {
                            System.out.print("새 이름: ");
                            String name = sc.nextLine();
                            System.out.print("새 이메일: ");
                            String email = sc.nextLine();
                            System.out.print("새 비밀번호: ");
                            String password = sc.nextLine();
                            User updated = userService.update(id,name, email, password);
                            System.out.println(updated != null ? " 유저 수정 완료" : " 대상을 찾을 수 없음");
                        } else if (updateType == 2) {
                            System.out.print("새 채널명: ");
                            String name = sc.nextLine();
                            System.out.print("새 설명: ");
                            String desc = sc.nextLine();
                            Channel updated = channelService.update(id, name, desc);
                            System.out.println(updated != null ? " 채널 수정 완료" : " 대상을 찾을 수 없음");
                        } else if (updateType == 3) {
                            System.out.print("새 메시지 내용: ");
                            String content = sc.nextLine();
                            Message updated = messageService.update(id, content);
                            System.out.println(updated != null ? " 메시지 수정 완료" : " 대상을 찾을 수 없음");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 UUID 형식입니다.");
                    }
                }

                // 4. 수정된 데이터 조회 (수정시간 UpdatedAt 확인)
                case 4 -> {
                    System.out.print("\n[4. 수정된 데이터 조회] 확인할 UUID 입력: ");
                    try {
                        UUID id = UUID.fromString(sc.nextLine().trim());
                        Optional<User> u = userService.read(id);
                        Optional<Channel> c = channelService.read(id);
                        Optional<Message> m = messageService.read(id);

                        if (u.isPresent()) {
                            User user = u.get();
                            System.out.println("[수정된 User 확인] 이름: " + user.getUsername() + ", 이메일: " + user.getEmail());
                            System.out.println(" ↳ CreatedAt: " + user.getCreatedAt() + " | UpdatedAt: " + user.getUpdatedAt());
                        } else if (c.isPresent()) {
                            Channel ch = c.get();
                            System.out.println("[수정된 Channel 확인] 이름: " + ch.getName() + ", 설명: " + ch.getDescription());
                            System.out.println(" ↳ CreatedAt: " + ch.getCreatedAt() + " | UpdatedAt: " + ch.getUpdatedAt());
                        } else if (m.isPresent()) {
                            Message msg = m.get();
                            System.out.println("[수정된 Message 확인] 내용: " + msg.getContent());
                            System.out.println(" ↳ CreatedAt: " + msg.getCreatedAt() + " | UpdatedAt: " + msg.getUpdatedAt());
                        } else {
                            System.out.println("일치하는 데이터가 없습니다.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 UUID 형식입니다.");
                    }
                }

                // 5. 삭제
                case 5 -> {
                    System.out.print("\n[5. 삭제] 삭제할 UUID 입력: ");
                    try {
                        UUID id = UUID.fromString(sc.nextLine().trim());
                        boolean deleted = userService.delete(id) || channelService.delete(id) || messageService.delete(id);
                        System.out.println(deleted ? " 데이터가 삭제되었습니다." : "삭제할 대상이 없습니다.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 UUID 형식입니다.");
                    }
                }

                // 6. 조회를 통해 삭제되었는지 확인
                case 6 -> {
                    System.out.print("\n[6. 삭제 여부 확인] 확인할 UUID 입력: ");
                    try {
                        UUID id = UUID.fromString(sc.nextLine().trim());
                        boolean exists = userService.read(id).isPresent()
                                || channelService.read(id).isPresent()
                                || messageService.read(id).isPresent();

                        if (!exists) {
                            System.out.println(" 확인 결과: 조회되지 않음 (정상적으로 삭제되었습니다).");
                        } else {
                            System.out.println("⚠️ 확인 결과: 데이터가 여전히 남아있습니다.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 UUID 형식입니다.");
                    }
                }

                // 0. 종료
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    running = false;
                }

                default -> System.out.println("잘못된 메뉴 번호입니다.");
            }
        }


        }

    }
