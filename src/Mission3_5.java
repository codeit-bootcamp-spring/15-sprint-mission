public class Mission3_5 {
    public static void main(String[] args) {
        int[] inputs = {10,20,30,0};
        int sum = 0;

        for (int num : inputs){
            System.out.println("값:" + num);

            if (num == 0) {
                break;
            }

            sum += num;
        }
        System.out.println("-------------");
        System.out.println("합계" + sum);

    }
}
