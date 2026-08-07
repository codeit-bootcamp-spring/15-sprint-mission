public class Mission3_7 {
    public static void main(String[] args) {

        System.out.println("1부터 20 까지의 소수:");

         for (int i = 2; i <= 20; i++){
             boolean isPrime = true;

             for (int j = 2; j < i; j++){
                 if (i % j == 0){
                     isPrime = false;
                     break;
                 }
             }

             if (isPrime){
                 System.out.println(i + " ");
             }
         }

    }
}
