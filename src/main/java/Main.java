import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        JCFUserService userServiceservice = JCFUserService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        System.out.println("유저 생성");
        userServiceservice.create(
                "test@test.com",
                "1234",
                "철수",
                NitroLevel.CLASSIC
        );

        userServiceservice.read();

        User testUser = userServiceservice.getTestUser();
        UUID testUserID = userServiceservice.getTestUser().getId();

        messageService.create(testUser,"와랄랄루");


        System.out.println("\n\n\n메세지 생성");
        messageService.read();


        messageService.update(messageService.getTestMessage().getId(),"dasdf");


        System.out.println("\n\n\n메세지 수정");
        messageService.read();

        messageService.delete(messageService.getTestMessage().getId());


        System.out.println("\n\n\n삭제");
        messageService.read();
    }
}
