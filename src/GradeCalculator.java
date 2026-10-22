public class GradeCalculator {
    public static void main(String[] args) {
        int score = 85;
        String grade;

        // if-else 문을 사용하여 학점 계산
        if (score >= 90) {
            grade = "A";
        } else if (score >= 80) {
            grade = "B";
        } else if (score >= 70) {
            grade = "C";
        } else if (score >= 60) {
            grade = "D";
        } else {
            grade="F";
        }
        
        System.out.println("점수: " + score + "점");
        System.out.println("학점: " + grade);
    }
}
