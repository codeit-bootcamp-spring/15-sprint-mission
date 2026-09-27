public class BMICalculator {
    public static void main(String[] args) {
        // 키와 몸무게 변수 선언 (키: 1.75m, 몸무게: 70kg)
        double height = 1.75;
        double weight = 70;
        // BMI 계산
        double BMI = weight / (height * height);
        // 결과 출력
        
        System.out.println("키: " + height + "m");
        System.out.println("몸무게: " + weight +"kg");
        System.out.println("BMI: " + BMI);
    }
}