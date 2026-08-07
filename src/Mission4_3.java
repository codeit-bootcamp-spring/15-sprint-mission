public class Mission4_3 {
    public static void main(String[] args) {

        int[] numbers = {3, 7, 2, 9, 1, 5, 8};

        int max = numbers[0];
        int min = numbers[0];

        for (int i = 1; i < numbers.length; i++){

            if (numbers[i] > max){
                max = numbers[i];
            }

            if (numbers[i] < min) {
                min = numbers[i];
            }
        }

        System.out.println("최댓값: " + max);
        System.out.println("최솟값: " + min);

    }
}
