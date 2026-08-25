import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;
import java.util.UUID;

public class Manager {

    private final static Manager instance = new Manager();

    private Manager() { }

    public static Manager getInstance() {
        return instance;
    }


    String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    Scanner sc = new Scanner(System.in);
    public void userManager(){

        String id;
        String email,password,name,nitro;
        NitroLevel nitroLevel;

        System.out.println("메뉴를 선택하세요 \n" +
                "1.유저 생성\n" +
                "2.유저 목록 출력\n" +
                "3.유저 정보 수정\n" +
                "4.유저 삭제");

        switch (sc.next()){

            case "1":
                System.out.println();
                System.out.println("E-mail을 입력하세요");
                email = sc.next();
                System.out.println("비밀번호를 입력하세요");
                password = sc.next();
                System.out.println("이름을 입력하세요");
                name = sc.next();
                System.out.println("니트로 레벨은 아래 목록이 있습니다.");
                for(NitroLevel level : NitroLevel.values()){
                    System.out.println(level);
                }
                //////////////////////////
                while (true) {

                    System.out.println("니트로 레벨을 입력하세요.");

                    nitro = sc.next();

                    try {
                        nitroLevel = NitroLevel.valueOf(nitro);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 니트로 레벨입니다. 다시 입력해주세요.");
                    }
                }
                ///////////////////////////


                JCFUserService.getInstance().create(email, password, name,
                        nitroLevel);
                break;

            case "2":
                JCFUserService.getInstance().read();
                break;

            case "3":
                System.out.println("XXX 입력 시 해당 항목을 수정하지 않습니다.");

                while (true) {
                    System.out.println("수정할 유저의 ID() : ");
                    id = sc.next();
                    if (id.matches(uuidPattern)) {
                        break;
                    } else {
                        System.out.println("UUID 형식이 아닙니다.");
                    }
                }


                System.out.println("E-mail을 입력하세요");
                email = sc.next();
                if(email.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            email=user.getEmail();
                        }
                    }
                }

                System.out.println("비밀번호를 입력하세요");
                password = sc.next();
                if(password.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            password=user.getPassword();
                        }
                    }
                }


                System.out.println("이름을 입력하세요");
                name = sc.next();
                if (name.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            name=user.getName();
                        }
                    }
                }


                System.out.println("니트로 레벨은 아래 목록이 있습니다.");
                for(NitroLevel level : NitroLevel.values()){
                    System.out.println(level);
                }
                while (true) {

                    System.out.println("니트로 레벨을 입력하세요.");

                    nitro = sc.next();

                    if (nitro.equals("XXX")){
                        for(User user : JCFUserService.getInstance().getUsers()){
                            if(user.getId().equals(UUID.fromString(id))){
                                nitro=user.getNitroLevel().toString();
                            }

                        }
                        if (nitro.equals("XXX")){
                            //nitro=NitroLevel.values()[0].toString();
                            throw new IllegalArgumentException("없는 id입니다. 니트로레벨을 읽어올 수 없습니다.");
                        }
                    }


                    try {
                        nitroLevel = NitroLevel.valueOf(nitro);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("잘못된 니트로 레벨입니다. 다시 입력해주세요.");
                    }
                }

                JCFUserService.getInstance().update(UUID.fromString(id),email,password,name,nitroLevel);
                break;

            case "4":
                System.out.println("삭제할 유저의 ID() : ");
                id = sc.next();

                JCFUserService.getInstance().delete(UUID.fromString(id));
                break;

            default:
                System.out.println("잘못된 입력입니다.");
                break;

        }


    }



    public void channelManager(){

        String id, messageId, userId;
        String name;
        ChannelRole channelRole;


        System.out.println("메뉴를 선택하세요 \n" +
                "1.채널 생성\n" +
                "2.채널 목록 출력\n" +
                "3.채널 정보 수정\n" +
                "4.채널내 메세지 출력\n" +
                "5.유저 등록/수정\n" +
                "6.유저 목록\n" +
                "7.유저 강퇴\n" +
                "8.채널 삭제");

        switch (sc.next()){

            case "1":
                System.out.println("이름을 입력하세요");
                name = sc.next();
                JCFChannelService.getInstance().create(name);
                break;

            case "2":
                JCFChannelService.getInstance().read();
                break;

            case "3":

                System.out.println("수정할 채널의 ID() : ");
                id = sc.next();

                System.out.println("이름을 입력하세요");
                name = sc.next();
                JCFChannelService.getInstance().update(UUID.fromString(id),name);

                //JCFUserService.getInstance().update(UUID.fromString(id),email,password,name,NitroLevel.valueOf(nitro));
                break;

                case "4":
                    System.out.println("출력할 채널의 ID() : ");
                    id = sc.next();
                    JCFChannelService.getInstance().printMessageList(UUID.fromString(id));
                    break;


            case "5"://유저 등록 갱신  putUser(UUID channelId, UUID userId, ChannelRole channelRole)
                System.out.println("등록할 채널의 ID() : ");
                id = sc.next();
                System.out.println("등록할 유저의 ID() : ");
                userId = sc.next();


                System.out.println("유저의 역할을 정해주세요 역할은 아래 목록이 있습니다.");
                for (ChannelRole role : ChannelRole.values()){
                    System.out.println(role);
                }
                try {
                    channelRole = ChannelRole.valueOf(sc.next());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("없는 역할입니다.");
                }


                JCFChannelService.getInstance().putUser(UUID.fromString(id),UUID.fromString(userId),channelRole);

                break;

            case "6":
                System.out.println("확인할 채널의 ID() : ");
                id = sc.next();
                JCFChannelService.getInstance().printUsers(UUID.fromString(id));
                break;

            case "7"://유저 강퇴removeUser(UUID channelId, UUID userId)

                System.out.println("강퇴할 채널의 ID() : ");
                id = sc.next();
                System.out.println("강퇴할 유저의 ID() : ");
                userId = sc.next();

                JCFChannelService.getInstance().removeUser(UUID.fromString(id),UUID.fromString(userId));

                break;

            case "8":
                System.out.println("삭제할 채널의 ID() : ");
                id = sc.next();

                JCFChannelService.getInstance().delete(UUID.fromString(id));
                break;

            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }


    }


    public void messageManager(){
        String id, channelId, userId;
        String text;
        Reaction reaction;


        System.out.println("메뉴를 선택하세요 \n" +
                "1.메세지 생성\n" +
                "2.메세지 목록 출력\n" +
                "3.메세지 수정\n" +
                "4.리액션(좋/싫)하기\n" +
                "5.리액션 출력\n" +
                "6.메세지 삭제");

        switch (sc.next()){

            case "1":
                System.out.println("보낼 채널의 ID를 입력하세요 : ");
                channelId = sc.next();

                System.out.println("보낼 유저의 ID를 입력하세요 : ");
                userId = sc.next();

                System.out.println("메세지 내용을 입력하세요 : ");
                text = sc.next();

                JCFMessageService.getInstance().create(UUID.fromString(channelId),UUID.fromString(userId),text);
                break;

            case "2":
                JCFMessageService.getInstance().read();
                break;

            case "3":

                System.out.println("수정할 메세지 ID() : ");
                id = sc.next();

                System.out.println("메세지 내용을 입력하세요");
                text = sc.next();
                JCFMessageService.getInstance().update(UUID.fromString(id),text);
                break;


            case "4"://toggleReaction(UUID messageId, UUID userId, Reaction reaction)
                System.out.println("반응할 메세지 ID() : ");
                id = sc.next();

                System.out.println("반응할 유저 ID() : ");
                userId = sc.next();

                System.out.println("반응을 정해주세요 반응은 아래 목록이 있으며 다시 토글하면 취소합니다.");
                for (Reaction re : Reaction.values()){
                    System.out.println(re);
                }
                try{
                    reaction = Reaction.valueOf(sc.next());
                }catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("없는 반응입니다");
                }

                JCFMessageService.getInstance().toggleReaction(UUID.fromString(id),UUID.fromString(userId),reaction);



                break;

            case "5"://printReactionCount(UUID messageid)
                System.out.println("출력할 메세지 ID() : ");
                id = sc.next();

                JCFMessageService.getInstance().printReactionCount(UUID.fromString(id));

                break;

            case "6":
                System.out.println("삭제할 메세지의 ID() : ");
                id = sc.next();

                JCFMessageService.getInstance().delete(UUID.fromString(id));

        }


    }


    public void createTestUnit() {

        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        // =========================
        // 유저 3명 생성
        // =========================

        userService.create(
                "test1@test.com",
                "1234",
                "유저1",
                NitroLevel.CLASSIC
        );
        UUID user1 = userService.getTestUser().getId();

        userService.create(
                "test2@test.com",
                "1234",
                "유저2",
                NitroLevel.CLASSIC
        );
        UUID user2 = userService.getTestUser().getId();

        userService.create(
                "test3@test.com",
                "1234",
                "유저3",
                NitroLevel.CLASSIC
        );
        UUID user3 = userService.getTestUser().getId();


        // =========================
        // 채널 3개 생성
        // =========================

        channelService.create("테스트채널1");
        UUID channel1 = channelService.getTestChannel().getId();

        channelService.create("테스트채널2");
        UUID channel2 = channelService.getTestChannel().getId();

        channelService.create("테스트채널3");
        UUID channel3 = channelService.getTestChannel().getId();


        UUID[] userIds = {
                user1,
                user2,
                user3
        };

        UUID[] channelIds = {
                channel1,
                channel2,
                channel3
        };


        // =========================
        // 모든 유저를 모든 채널에 등록
        // =========================

        for (UUID channelId : channelIds) {
            for (UUID userId : userIds) {
                channelService.putUser(
                        channelId,
                        userId,
                        ChannelRole.MEMBER
                );
            }
        }


        // =========================
        // 메시지 9개 생성
        // =========================

        UUID[][] messageIds = new UUID[3][3];

        for (int userIndex = 0; userIndex < userIds.length; userIndex++) {

            for (int channelIndex = 0;
                 channelIndex < channelIds.length;
                 channelIndex++) {

                messageService.create(
                        channelIds[channelIndex],
                        userIds[userIndex],
                        "유저" + (userIndex + 1)
                                + "-채널" + (channelIndex + 1)
                                + " 메시지"
                );

                messageIds[userIndex][channelIndex] =
                        messageService.getTestMessage().getId();
            }
        }


        // =========================
        // testUnitID.txt 작성
        // =========================

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < userIds.length; i++) {
            text.append("유저")
                    .append(i + 1)
                    .append(" : ")
                    .append(userIds[i])
                    .append(System.lineSeparator());
        }

        text.append(System.lineSeparator());

        for (int i = 0; i < channelIds.length; i++) {
            text.append("채널")
                    .append(i + 1)
                    .append(" : ")
                    .append(channelIds[i])
                    .append(System.lineSeparator());
        }

        text.append(System.lineSeparator());

        for (int userIndex = 0; userIndex < messageIds.length; userIndex++) {

            for (int channelIndex = 0;
                 channelIndex < messageIds[userIndex].length;
                 channelIndex++) {

                text.append("유저")
                        .append(userIndex + 1)
                        .append("-채널")
                        .append(channelIndex + 1)
                        .append(" 메세지 : ")
                        .append(messageIds[userIndex][channelIndex])
                        .append(System.lineSeparator());
            }
        }


        // =========================
        // 파일 저장
        // =========================

        try {
            java.nio.file.Path path =
                    java.nio.file.Paths.get(
                            "src",
                            "main",
                            "메모",
                            "testUnitID.txt"
                    );

            java.nio.file.Files.writeString(
                    path,
                    text.toString(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

        } catch (java.io.IOException e) {
            throw new RuntimeException("testUnitID.txt 생성 실패", e);
        }
    }

}
