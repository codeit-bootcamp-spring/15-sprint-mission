package com.sprint.mission.discodeit;

import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
// 여기서 서비스 객체 생성 및 테스트 코드 작성
        boolean running = true;
        Scanner sc = new Scanner(System.in);

        while (running) {
            System.out.println("===== 짭스코드 =====");
            System.out.println("1. 로그인");
            System.out.println("2. 계정 만들기");
            System.out.println("0. 종료");


            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: // 로그인메뉴
                    System.out.println("1. 아이디");
                    System.out.println("2. 비밀번호");


                    break;
                case 2: // 계정 만들기 메뉴
                    System.out.println("새로운 계정을 생성합니다.");

                    break;
                case 0: //프로그램 종료 메뉴
                    System.out.println("프로그램을 종료합니다");
                    running = false;
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }


    }
}