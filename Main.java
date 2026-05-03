import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Car  Car1 = new Car.CarBuilder("Vesta", 100,2025).build();
        Car  Car2 = new Car.CarBuilder("Granta", 100,2021).build();
        Car  Car3 = new Car.CarBuilder("Solaris",120 ,2023).build();
        Car  Car4 = new Car.CarBuilder("X5",530,2025).build();
        Car  Car5 = new Car.CarBuilder("Rio",130,2023).build();
        Car  Car6 = new Car.CarBuilder("Rio",130,2024).build();
        Car  Car7 = new Car.CarBuilder("Rio",130,2025).build();
        Car  Car8 = new Car.CarBuilder("Aveo",130,2025).build();
        Car  Car9 = new Car.CarBuilder("Accord",280,2024).build();
        Car Car10 = new Car.CarBuilder("Baleno",280,2024).build();
        Car Car11 = new Car.CarBuilder("Creta",280,2024).build();
        Car Car12 = new Car.CarBuilder("Duster",280,2024).build();
        Car Car13 = new Car.CarBuilder("Duster",230,2018).build();
        Car Car14 = new Car.CarBuilder("Kaptur",180,2022).build();
        Car Car15 = new Car.CarBuilder("Vitara",200,2016).build();

        Car[] arrayCars = new Car[15];

        arrayCars[0] = Car1;
        arrayCars[1] = Car2;
        arrayCars[2] = Car3;
        arrayCars[3] = Car4;
        arrayCars[4] = Car5;
        arrayCars[5] = Car6;
        arrayCars[6] = Car7;
        arrayCars[7] = Car8;
        arrayCars[8] = Car9;
        arrayCars[9] = Car10;
        arrayCars[10] = Car11;
        arrayCars[11] = Car12;
        arrayCars[12] = Car13;
        arrayCars[13] = Car14;
        arrayCars[14] = Car15;

        ArraySorter arraySorter = new ArraySorter(new SelectionSort());
        arraySorter.sort(arrayCars);
        System.out.println(Arrays.toString(arrayCars));
    }
}
