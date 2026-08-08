public class MinMaxFinder {
    public static void main(String[] args) {
        // 배열 선언 및 초기화
        int[] arr = {3, 7, 2, 9, 1, 5, 8};
        // 최댓값과 최솟값 찾기
        int max = arr[0];
        int min = 10000;
        for (int a=0; a < arr.length ;a++) {
            if (arr[a] > max) {
                max = arr[a];
            } else if (arr[a] < min) {
                min = arr[a];
            }
        }
        
        System.out.println("최댓값: " + max);
        System.out.println("최솟값: "+ min);
    }
}
