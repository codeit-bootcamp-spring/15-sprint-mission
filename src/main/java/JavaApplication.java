import com.sprint.mission.discodeit.repository.ChannelJSON;
import com.sprint.mission.discodeit.repository.MessageJSON;
import com.sprint.mission.discodeit.repository.UserJSON;

import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {

        String choose;
        boolean loop = true;





        Scanner sc = new Scanner(System.in);
        while (loop) {
            try{
                System.out.println("관리 대상을 선택하세요 \n" +
                        "1. 유저\n" +
                        "2. 채널\n" +
                        "3. 메세지\n" +
                        "4. 저장\n" +
                        "5. 불러오기\n" +
                        "6. 종료\n" +
                        "0. 테스트유닛생성");
                choose=sc.next();
                switch (choose){
                    case "1":
                        Manager.getInstance().userManager();
                        break;

                    case "2":
                        Manager.getInstance().channelManager();
                        break;


                    case "3":
                        Manager.getInstance().messageManager();
                        break;

                    case "4":
                        UserJSON.SaveUser();
                        MessageJSON.SaveMessage();
                        ChannelJSON.SaveChannel();
                        break;


                    case "5":
                        UserJSON.LoadUser();
                        MessageJSON.LoadMessage();
                        ChannelJSON.LoadChannel();
                        break;

                    case "6":
                        loop=false;
                        break;

                    case "0":
                        Manager.getInstance().createTestUnit();
                        break;

                    default:
                        System.out.println("잘못된 입력입니다.");

                        break;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }


    }
}
