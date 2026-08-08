public class MultiplicationTable {
    public static void main(String[] args) {
        // 중첩 for문을 사용하여 구구단 출력
        for (int i = 2; i <= 9;i++) {
            System.out.println("=== " + i + "단 "+ "===");
            for (int j=1; j<=9; j++){
                System.out.println(i + " × " + j + " = " + i*j);
            }
            System.out.println();
        }
    }
}
