import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        JCFUserService userServiceservice = JCFUserService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        System.out.println("유저 생성");
        userServiceservice.create(
                "test@test.com",
                "1234",
                "철수",
                NitroLevel.CLASSIC
        );





        userServiceservice.read();

      //  User testUser = userServiceservice.getTestUser();
      //  UUID testUserID = userServiceservice.getTestUser().getId();

        messageService.create(userServiceservice.getTestUser().getId(),"와랄랄루");


        System.out.println("\n\n\n메세지 생성");
        messageService.read();


        messageService.toggleReaction(messageService.getTestMessage().getId(),
                userServiceservice.getTestUser().getId(),
                Reaction.ANGRY);

        userServiceservice.create(
                "test@test.com",
                "1234",
                "철수2",
                NitroLevel.CLASSIC
        );

        messageService.toggleReaction(messageService.getTestMessage().getId(),
                userServiceservice.getTestUser().getId(),
                Reaction.ANGRY);


        messageService.delete_UserToMessage(userServiceservice.getTestUser().getId());


        userServiceservice.create(
                "test@test.com",
                "1234",
                "철수3",
                NitroLevel.CLASSIC
        );

        messageService.toggleReaction(messageService.getTestMessage().getId(),
                userServiceservice.getTestUser().getId(),
                Reaction.ANGRY);





        System.out.println(messageService.getReactionCount(messageService.getTestMessage().getId(),
                Reaction.ANGRY));

        messageService.toggleReaction(messageService.getTestMessage().getId(),
                userServiceservice.getTestUser().getId(),
                Reaction.ANGRY);

        System.out.println(messageService.getReactionCount(messageService.getTestMessage().getId(),
                Reaction.ANGRY));



    }
}
