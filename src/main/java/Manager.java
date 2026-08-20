import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;

public class Manager {

    private final static Manager instance = new Manager();

    private Manager() { }

    public static Manager getInstance() {
        return instance;
    }

    int choose;

    String email,password,name,nitro;

    Scanner sc = new Scanner(System.in);
    public void userManager(){
        System.out.println("메뉴를 선택하세요 \n" +
                "1.유저 생성\n" +
                "2.유저 목록 출력\n" +
                "3.유저 정보 수정\n" +
                "4.유저 삭제");

        switch (sc.nextInt()){

            case 1:
                System.out.println();
                System.out.println("E-mail을 입력하세요");
                email = sc.next();
                System.out.println("비밀번호를 입력하세요");
                password = sc.next();
                System.out.println("이름을 입력하세요");
                name = sc.next();
                System.out.println("니트로 레벨은 아래 목록이 있습니다.");
                for(NitroLevel level : NitroLevel.values()){
                    System.out.println(level);
                }
                System.out.println("니트로 레벨을 입력하세요");
                nitro = sc.next();


                JCFUserService.getInstance().create(email, password, name,
                        NitroLevel.valueOf(nitro));
                break;

                case 2:
                    JCFUserService.getInstance().read();
                    break;
        }


    }



    public void channelManager(){

    }



    public void messageManager(){


    }

}
