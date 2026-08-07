public class Mission2_3 {
    public static void main(String[] args) {

        double height = 175.5;
        double weight = 70.0;

        double heightInMerers = height / 100;

        double bmi = weight / (heightInMerers * heightInMerers);

        System.out.println("키:" + height + "cm");
        System.out.println("몸무게:" + weight + "kg");
        System.out.println("BMI" + bmi);



    }
}
