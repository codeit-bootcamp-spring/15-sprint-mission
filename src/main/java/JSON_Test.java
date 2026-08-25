import com.sprint.mission.discodeit.FileIO.ChannelJSON;
import com.sprint.mission.discodeit.FileIO.MessageJSON;
import com.sprint.mission.discodeit.FileIO.UserJSON;

public class JSON_Test {

    public static void main(String[] args) {
        Manager.getInstance().createTestUnit();
        UserJSON.SaveUser();
        MessageJSON.SaveMessage();
        ChannelJSON.SaveChannel();
    }
}
