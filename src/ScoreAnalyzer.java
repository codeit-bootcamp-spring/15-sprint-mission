public class ScoreAnalyzer {
    public static void main(String[] args) {
        // 배열 선언 및 초기화
        int[] scores = {85, 90, 78, 92, 88};
        // 평균 계산
        int sum = 0;
        double avg;
        for (int a : scores) {
            sum += a;
        }
        avg = (double) sum / scores.length;
        System.out.println("평균: " + avg);
        int high=0, low;
        for (int i=0; i < scores.length; i++) {
            if (scores[i] > avg){
                high += 1;
            }
        }
        low = scores.length - high;
        System.out.println("평균보다 높은 점수 개수: "+high);
        System.out.println("평균보다 낮은 점수 개수: "+low);

        // 평균보다 높은/낮은 점수 개수 세기
    }
}
