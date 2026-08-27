package test;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.Scanner;
import java.util.UUID;

public class ManagerTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Manager.getInstance().createTestUnit();

        JCFChannelService.getInstance().printMessageList(UUID.fromString(sc.next()));

        Manager.getInstance().userManager();

    //    test.Manager.getInstance().channelManager();
    //    test.Manager.getInstance().channelManager();
    //    test.Manager.getInstance().channelManager();
    //    test.Manager.getInstance().channelManager();

        Manager.getInstance().userManager();

        Manager.getInstance().userManager();


        Manager.getInstance().userManager();

        Manager.getInstance().userManager();
    }
}
