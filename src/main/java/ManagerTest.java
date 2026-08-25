import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.UserService;

import java.util.Scanner;
import java.util.UUID;

public class ManagerTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Manager.getInstance().createTestUnit();

        JCFChannelService.getInstance().printMessageList(UUID.fromString(sc.next()));

        Manager.getInstance().userManager();

    //    Manager.getInstance().channelManager();
    //    Manager.getInstance().channelManager();
    //    Manager.getInstance().channelManager();
    //    Manager.getInstance().channelManager();

        Manager.getInstance().userManager();

        Manager.getInstance().userManager();


        Manager.getInstance().userManager();

        Manager.getInstance().userManager();
    }
}
