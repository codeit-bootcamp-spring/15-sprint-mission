import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class Test {
    public static void main(String[] args) {

        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        // ========================================
        // 1. User 생성
        // ========================================

        userService.create(
                "test@test.com",
                "1234",
                "철수",
                NitroLevel.CLASSIC
        );

        System.out.println("===== User =====");
        userService.read();


        // ========================================
        // 2. Channel 생성
        // ========================================

        channelService.create("테스트 채널");

        Channel channel = channelService.getTestChannel();

        System.out.println("\n===== Channel 생성 =====");
        channelService.read();


        // ========================================
        // 3. User를 Channel에 추가
        // ========================================

        User user = null;

        // 현재 UserService의 create()가 void이므로
        // 일단 read()로 UUID를 확인한 뒤 직접 넣어야 함.
        //
        // 테스트 편의를 위해 아래 UUID에
        // read()에서 확인한 User UUID를 입력

        String userIdString = userService.getTestUser().getId().toString();

        System.out.println(userIdString);

        try {

            java.util.UUID userId =
                    java.util.UUID.fromString(userIdString);

            channelService.putUser(
                    channel.getId(),
                    userId,
                    ChannelRole.ADMIN
            );


            // ========================================
            // 4. Message 생성
            // ========================================

            messageService.create(
                    channel.getId(),
                    userId,
                    "첫 번째 메시지"
            );

            Message message1 = messageService.getTestMessage();

            messageService.create(
                    channel.getId(),
                    userId,
                    "두 번째 메시지"
            );

            Message message2 = messageService.getTestMessage();


            System.out.println("\n===== Message 생성 =====");
            messageService.read();


            // ========================================
            // 5. Channel에 Message가 들어갔는지 확인
            // ========================================

            System.out.println("\n===== Channel의 Message UUID =====");

            for (UUID messageId :
                    channelService.getMessagesListMap().get(channel.getId())) {

                System.out.println(messageId);
            }


            // ========================================
            // 6. Reaction 추가
            // ========================================

            messageService.toggleReaction(
                    message1.getId(),
                    userId,
                    Reaction.LIKE
            );

            System.out.println("\n===== Reaction =====");

            messageService.printReactionCount(
                    message1.getId()
            );


            // ========================================
            // 7. Message 삭제
            // ========================================

            System.out.println("\n===== Message 삭제 전 =====");
            messageService.read();

            messageService.delete(message1.getId());

            System.out.println("\n===== Message 삭제 후 =====");
            messageService.read();

            System.out.println("\n===== Channel Message 목록 =====");

            for (java.util.UUID messageId :
                    channelService.getMessagesListMap().get(channel.getId())) {

                System.out.println(messageId);
            }


            // ========================================
            // 8. Channel 삭제
            // ========================================

            System.out.println("\n===== Channel 삭제 =====");

            channelService.delete(channel.getId());

            System.out.println("\nChannel 삭제 후:");

            channelService.read();

            System.out.println("\nMessage 목록:");
            messageService.read();


        } catch (IllegalArgumentException e) {

            System.out.println("오류: " + e.getMessage());
        }
    }
}