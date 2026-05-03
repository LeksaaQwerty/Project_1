

public class Main {
    
    public static void main(String[] args) {
        

        Car car1 = new Car.Builder("Tesla Model S")
            .build();
    
        Car car2 = new Car.Builder("BMW M5 Com")
            .hp(600)
            .yearOfProduction(2019)
            .build();
        
        System.out.println(car1);
        System.out.println(car2);
        

            
    }
}
