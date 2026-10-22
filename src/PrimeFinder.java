public class PrimeFinder {
    public static void main(String[] args) {
        // 1부터 20까지의 소수 찾기
        System.out.println("1부터 20까지의 소수:");
        
        // 각 숫자에 대해 소수 판별
        // 2
        // 1, 2
        for (int i = 2; i <= 20;i++) { 
            boolean isPrime = true;
            for (int j = 2; j<i; j++) { // 자신이 포함되면 안됨.
                if (i%j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime == true){
                System.out.print(i + " ");
            }
        } 
    }
}
