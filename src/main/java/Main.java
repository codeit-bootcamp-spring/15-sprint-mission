import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

public class Main {
    public static void main(String[] args) {
        User user = new User("2314@asdf.com","asd","asdf", NitroLevel.CLASSIC);

        System.out.println(user.getId());
        System.out.println(user.getUpdatedAt());
    }
}
