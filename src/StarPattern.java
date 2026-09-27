public class StarPattern {
    public static void main(String[] args) {
        //패턴 출력
        for (int i=1; i <= 5; i++) {
            for (int j=1; j<=i; j++){
                System.out.print("*");
            }
            System.out.println();
        }
        
        for (int i=6; i >= 1; i--) {
            for (int j=1; j<=i; j++){
                System.out.print("*");
            }
            System.out.println();
        }
        
    }
}
