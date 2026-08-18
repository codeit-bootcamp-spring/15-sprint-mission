import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
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

        User testUser = userServiceservice.getTestUser();
        UUID testUserID = userServiceservice.getTestUser().getId();

        messageService.create(testUser,"와랄랄루");


        System.out.println("\n\n\n메세지 생성");
        messageService.read();


        System.out.printf("\n\n\n채널 생성");
        channelService.create("자바15");
        channelService.read();




        System.out.printf("\n\n\n채널 업뎃");
        channelService.update(channelService.getTestChannel().getId(),"스프링15");
        channelService.read();

        System.out.printf("\n\n\n채널 삭제");
        channelService.delete(channelService.getTestChannel().getId());
        channelService.read();

    }
}
