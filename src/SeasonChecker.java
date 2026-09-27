public class SeasonChecker {
    public static void main(String[] args) {
        int month = 7;
        String season = new String();

        // switch문을 사용하여 계절 판단
        switch (month) {
            case 3:
            case 4:
            case 5:
                season = "봄";
                break;
            case 6:
            case 7:
            case 8:
                season = "여름";
                break;
            case 9:
            case 10:
            case 11:
                season = "가을";
                break;
            default:
                season = "겨울";
        }
        System.out.println(month + "월은 " + season + "입니다.");
    }
}
