import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {

        int choose;
        boolean loop = true;



        Scanner sc = new Scanner(System.in);
        while (loop) {
            System.out.println("관리 대상을 선택하세요 \n" +
                    "1. 유저\n" +
                    "2. 채널\n" +
                    "3. 메세지\n" +
                    "4. 종료");
            choose=sc.nextInt();
            switch (choose){
                case 1:
                    Manager.getInstance().userManager();
                    break;

                case 2:
                    Manager.getInstance().channelManager();
                    break;


                case 3:
                    Manager.getInstance().messageManager();
                    break;

                case 4:
                    loop=false;
                    break;

                default:
                    System.out.println("잘못된 입력입니다.");

                    break;
            }
        }


    }
}
