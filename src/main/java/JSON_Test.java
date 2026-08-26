import com.sprint.mission.discodeit.repository.ChannelJSON;
import com.sprint.mission.discodeit.repository.MessageJSON;
import com.sprint.mission.discodeit.repository.UserJSON;

public class JSON_Test {

    public static void main(String[] args) {
        Manager.getInstance().createTestUnit();
        UserJSON.SaveUser();
        MessageJSON.SaveMessage();
        ChannelJSON.SaveChannel();
     //   UserData userData = UserJSON.LoadUser();
     //   System.out.println(userData);
     //   UserJSON.LoadUser();
     //   MessageJSON.LoadMessage();
     //   ChannelJSON.LoadChannel();
        //System.out.println("asdf");
        Manager.getInstance().userManager();
        Manager.getInstance().messageManager();
        Manager.getInstance().channelManager();

    }
}
