package com.sprint.mission;
import java.util.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class JavaApplication {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 각 도메인별 CRUD기능 사용을 위한 인스턴스 선언
//        UserService userService = new JCFUserService();     // 메모리에 JCFUserService객체를 만들고, 그 주소를 userService라는 변수에 담을거야
//        ChannelService channelService = new JCFChannelService();    //
//        MessageService messageService = new JCFMessageService();    //

        /// FileIO
        UserService userService = new FileUserService();
        ChannelService channelService = new FileChannelService();
        MessageService messageService = new FileMessageService();

//        User user = setupUser(userService);
//        Channel channel = setupChannel(channelService);
//        // 테스트
//        messageCreateTest(messageService, channel, user);

        int count = 0;
        boolean running = true;
        boolean registered = false;

        while (running) {
            System.out.println("========discodeit========");
            System.out.println("1. 등록");
            System.out.println("2. 조회");
            System.out.println("3. 수정");
            System.out.println("4. 삭제");
            System.out.println("0. 종료");
            System.out.print("선택> ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("숫자만 입력할 수 있습니다.\n");
                sc.nextLine();  // 입려된 문자(버퍼) 안 비우면 무한루프 걸림
                continue;   // 메뉴 처음으로
            }
            switch (choice) {
                case 1:
                    System.out.println("1. User 등록   2. Channer 등록   3. Message 등록  0. 뒤로가기");
                    System.out.print("등록> ");
                    int register = sc.nextInt();
                    sc.nextLine();
                    try {   // 1. 등록 try시작
                        switch (register) {
                            case 1: // User 등록
                                System.out.print("유저 이름을 입력하세요: ");
                                String userName = sc.nextLine();

                                // userService가 JCF를 쓰니까 JCF안에 Map에 String userName이 저장
                                userService.createUser(userName);
                                System.out.println("유저 등록이 완료되었습니다.");
                                System.out.println();
                                break;
                            case 2:
                                // Channel 등록
                                System.out.print("채널명을 입력하세요: ");
                                String channelName = sc.nextLine();
                                // channelService가 JCF를 쓰니까 JCF안에 Map에 String channelName이 저장
                                channelService.createChannel(channelName);
                                System.out.println("채널 등록이 완료되었습니다.");
                                break;
                            case 3:
                                // Message 등록
                                System.out.print("메시지를 입력하세요: ");
                                String content = sc.nextLine();
                                messageService.createMessage(content);
                                System.out.println("메시지 등록이 완료되었습니다.");
                                break;
                            default:
                                break; // 뒤로가기(취소)
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 형식입니다.");
                    }   // 1. 등록 try종료
                    break;  // case 1 종료
                case 2: // 조회
                    System.out.println("1. ID로 조회   2. 전체 조회   0. 뒤로가기");
                    int search = sc.nextInt();
                    sc.nextLine();
                    try {   // 2. 조회 try 시작
                        switch (search) {
                            case 1:     // ID로 조회
                                try {
                                    System.out.println("------ID로 조회------");
                                    System.out.print("조회할 ID> ");
                                    String searchID = sc.nextLine();

                                    // 문자열을 UUID 객체로 변환
                                    // 생성된 id는 그냥두면 string쪼가리임.
                                    // 얘를 Java가 인식하는 UUID형식으로 바꿔주는(파싱)거임
                                    UUID id = UUID.fromString(searchID);    //그게 이 한 줄임
                                    // 각 서비스에서 ID로 데이터 가져오기
                                    User searchUser = userService.getUser(id);
                                    Channel searchChannel = channelService.getChannel(id);
                                    Message searchMessage = messageService.getMessage(id);
                                    if(searchUser != null) System.out.println("[해당 유저 정보]" + searchUser + searchUser.getCreatedAt());
                                    else if (searchChannel != null) {
                                        System.out.println("[해당 채널 정보]" + searchChannel + searchChannel.getCreatedAt());
                                    } else if (searchMessage != null) {
                                        System.out.println("[해당 메시지 정보]" +searchMessage + searchMessage.getCreatedAt());
                                    }
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 UUID 형식입니다. (예: 8c158897-bcf9-4539-a901-44736fdb7838)");
                                } catch (Exception e) {
                                    System.out.println("조회 중 오류발생" + e.getMessage());
                                }
                                break;

                            case 2:     // 전체 조회
                                System.out.println("------전체 조회------");
                                System.out.println("유저 정보:\n"+ userService.getAllUsers());
                                System.out.println("채널 정보: \n" + channelService.getAllChannels());
                                System.out.println("메시지 정보: \n" + messageService.getAllMessages());
                                break;
                            default: break;     // 나가기
                        }
                    }catch (IllegalArgumentException e) {
                        System.out.println("잘못된 형식입니다.");
                    }   // 2. 조회 try 종료

                    break;
                case 3:     // 수정
                    System.out.println("1. User 수정   2. Channer 수정   3. Message 수정  0. 뒤로가기");
                    int modify = sc.nextInt();  // 선택
                    sc.nextLine();  // 초기화
                    try {   // 3. 수정 try 시작
                        switch (modify) {
                            case 1:
                                System.out.println("------User 수정------");
                                System.out.print("수정할 대상(ID, 모르면 0): ");
                                String userID = sc.nextLine();
                                if(userID.equals("0")) break;
                                try {
                                    UUID mUser = UUID.fromString(userID);    // UUID변환
                                    if(userService.getUser(mUser) != null) {
                                        System.out.print("수정 내용: ");
                                        String modified = sc.nextLine();
                                        userService.updateUser(mUser, modified);
                                        System.out.println("수정이 완료되었습니다.");
                                        break;
                                    } else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다.");
                                }

                                ///왜 Map.put을 다시 안 할까?
                                /// userMap.get(id)로 꺼내온 user 변수는 Map 속에 들어있는 '진짜 유저 객체'의 주소를 가리키고 있음.
                                ///따라서 user.updateUserName(...)을 실행하는 순간
                                ///Map이 가리키고 있던 실제 객체의 데이터가 바로 변한다~
                                break;  // case 1 종료
                            case 2:
                                System.out.println("------Channel 수정------");
                                System.out.print("수정할 대상(ID, 모르면 0): ");
                                String channelID = sc.nextLine();
                                if(channelID.equals("0")) break;
                                try {
                                    UUID mChannel = UUID.fromString(channelID);
                                    if(channelService.getChannel(mChannel) != null) {
                                        System.out.print("수정 내용: ");
                                        String modified = sc.nextLine();
                                        channelService.updateChannel(mChannel, modified);
                                        System.out.println("수정이 완료되었습니다.");
                                        break;
                                    } else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다."); }

                                break;  // case 2 종료
                            case 3:
                                System.out.println("------Message 수정------");
                                System.out.print("수정할 대상(ID, 모르면 0): ");
                                String messageID = sc.nextLine();
                                if(messageID.equals("0")) break;
                                try {
                                    UUID mMessage = UUID.fromString(messageID);
                                    if(messageService.getMessage(mMessage) != null) {
                                        System.out.print("수정 내용: ");
                                        String modified = sc.nextLine();
                                        messageService.updateMessage(mMessage, modified);
                                        System.out.println("수정이 완료되었습니다.");
                                        break;
                                    } else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다.");}
                                break;  // case 3 종료
                            default: break; // 나가기
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 형식입니다.");
                    }   // 3. 수정 try 종료

                    break;
                case 4:
                    System.out.println("1. User 삭제   2. Channer 삭제   3. Message 삭제   0. 뒤로가기");
                    int delete = sc.nextInt();
                    sc.nextLine();
                    try {
                        switch (delete) {
                            case 1:
                                System.out.println("------User 삭제------");
                                System.out.println("삭제할 대상(ID) or 취소(0): ");
                                String rmU = sc.nextLine();
                                if(rmU.equals("0")) break;
                                try {
                                    UUID uDelete = UUID.fromString(rmU);   // UUID값 String 으로 변경
                                    if(userService.getUser(uDelete) != null) {
                                        userService.deleteUser(uDelete);
                                        System.out.println("유저가 삭제되었습니다.");
                                        break;
                                    } else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다.");
                                }
                                break;  // delete_case 1: 종료
                            case 2:
                                System.out.println("------Channel 삭제------");
                                System.out.println("삭제할 대상(ID) or 취소(0): ");
                                String rmC = sc.nextLine();
                                if(rmC.equals("0")) break;
                                try {
                                    UUID cDelete = UUID.fromString(rmC);
                                    if(channelService.getChannel(cDelete) != null) {
                                        channelService.deleteChannel(cDelete);
                                        System.out.println("채널이 삭제되었습니다.");
                                        break;
                                    } else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다.");
                                }
                                break;  // delete_case 2: 종료
                            case 3:
                                System.out.println("------Message 삭제------");
                                System.out.println("삭제할 대상(ID) or 취소(0): ");
                                String rmM = sc.nextLine();
                                if(rmM.equals("0")) break;
                                try {
                                    UUID mDelete = UUID.fromString(rmM);
                                    if(messageService.getMessage(mDelete) != null) {
                                        messageService.deleteMessage(mDelete);
                                        System.out.println("메시지가 삭제되었습니다.");
                                        break;
                                    }else System.out.println("해당 ID를 찾을 수 없습니다.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 형식입니다.");
                                }

                                break;  // delete_case 3: 종료
                            default: break; // 삭제 취소 -> 메인 메뉴로 돌아감
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 형식입니다.");
                    }   // 4. 삭제 try종료
                    break;
                default: {
                    System.out.println("종료합니다.");
                    running = false;
                }
            }
        }
    }
}
/// 중복되는 코드가 많아 클래스로 묶어서 사용해도 되는거 아닌가 하는 의문이 듦.