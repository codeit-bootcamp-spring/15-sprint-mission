package test;

import com.sprint.mission.discodeit.service.jcf.JSON.ChannelJSON;
import com.sprint.mission.discodeit.service.jcf.JSON.MessageJSON;
import com.sprint.mission.discodeit.service.jcf.JSON.UserJSON;

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
