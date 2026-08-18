import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class Main {
    public static void main(String[] args) {

        JCFUserService service = JCFUserService.getInstance();

        System.out.println("크리에이트");
        service.create(
                "test@test.com",
                "1234",
                "철수",
                NitroLevel.CLASSIC
        );

        service.read();


        System.out.println("업데이트");
        service.update(service.getTestUser().getId(), "2qwer@we.cr","1235","asr",NitroLevel.BASIC);

        service.read();


        System.out.println("델리트");
        service.delete(service.getTestUser().getId());

        service.read();


    }
}
