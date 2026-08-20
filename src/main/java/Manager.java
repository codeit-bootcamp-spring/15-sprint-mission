import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;
import java.util.UUID;

public class Manager {

    private final static Manager instance = new Manager();

    private Manager() { }

    public static Manager getInstance() {
        return instance;
    }


    String id;
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

            case 3:
                System.out.println("XXX 입력 시 해당 항목을 수정하지 않습니다.");
                System.out.println("수정할 유저의 ID() : ");
                id = sc.next();

                System.out.println("E-mail을 입력하세요");
                email = sc.next();
                if(email.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            email=user.getEmail().toString();
                        }
                    }
                }

                System.out.println("비밀번호를 입력하세요");
                password = sc.next();
                if(password.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            password=user.getPassword().toString();
                        }
                    }
                }


                System.out.println("이름을 입력하세요");
                name = sc.next();
                if (name.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            name=user.getName().toString();
                        }
                    }
                }


                System.out.println("니트로 레벨은 아래 목록이 있습니다.");
                for(NitroLevel level : NitroLevel.values()){
                    System.out.println(level);
                }
                System.out.println("니트로 레벨을 입력하세요");
                nitro = sc.next();
                if (nitro.equals("XXX")){
                    for(User user : JCFUserService.getInstance().getUsers()){
                        if(user.getId().equals(UUID.fromString(id))){
                            nitro=user.getNitroLevel().toString();
                        }
                    }
                }


                JCFUserService.getInstance().update(UUID.fromString(id),email,password,name,NitroLevel.valueOf(nitro));
                break;

            case 4:
                System.out.println("삭제할 유저의 ID() : ");
                id = sc.next();

                JCFUserService.getInstance().delete(UUID.fromString(id));

        }


    }



    public void channelManager(){

    }



    public void messageManager(){


    }

}
