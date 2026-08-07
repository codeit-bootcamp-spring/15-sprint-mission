public class Mission4_4 {
    public static void main(String[] args) {

        int[] scores = {85, 90, 78, 92, 88};

        int sum = 0;

        for (int score : scores) {
            sum += score;
        }

        double average = (double) sum / scores.length;

        int aboceAverage = 0;
        int belowAverage = 0;

        for (int score : scores) {
            if (score > average) {
                aboceAverage++;
            } else if (score < average) {
                belowAverage++;

            }
        }

        System.out.println("평균: " + average );
        System.out.println("평균보다 높은 점수 개수: " + aboceAverage );
        System.out.println("평균보다 낮은 점수 개수: " + belowAverage );
    }
}
