public class TemperatureConverter {
    public static void main(String[] args) {
        int celsius = 25;
    
        // 화씨 변환 (정확한 실수 계산)
        double Fahrenheit = celsius * 9/5 + 32; 
        // 켈빈 변환
        double Kelvin = celsius + 273.15;

        // 결과 출력
        System.out.println("섭씨: "+ celsius);
        System.out.println("화씨: "+ Fahrenheit);
        System.out.println("켈빈: "+ Kelvin);
    }
}
