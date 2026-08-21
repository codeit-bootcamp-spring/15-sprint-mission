import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.UserService;

public class ManagerTest {
    public static void main(String[] args) {

        Manager.getInstance().createTestUnit();

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
