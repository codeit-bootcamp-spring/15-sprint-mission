import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(
                "user1@test.com",
                "1234",
                "철수",
                NitroLevel.CLASSIC
        );

        User user2 = new User(
                "user2@test.com",
                "1234",
                "영희",
                NitroLevel.NORMAL
        );

        Channel channel = new Channel();

        // 유저 권한 등록
        channel.setRole(user1, ChannelRole.OWNER);
        channel.setRole(user2, ChannelRole.ADMIN);

        // 권한 확인
        System.out.println(channel.getRole(user1)+"\t1");
        System.out.println(channel.getRole(user2)+"\t2");

        // 권한 수정
        channel.setRole(user2, ChannelRole.MEMBER);

        System.out.println(channel.getRole(user2)+"\t3");

        // 유저 제거
        channel.removeUser(user1);

        System.out.println(channel.getRole(user1)+"\t4");
    }
}
