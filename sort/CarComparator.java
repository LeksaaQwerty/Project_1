package sort;

import java.util.Comparator;
import model.Car;

public class CarComparator {


    public static Comparator<Car> byHp() {
        return Comparator.comparing(Car::getHp);
    }

    public static Comparator<Car> byYear() {
        return Comparator.comparing(Car::getYearOfProduction);
    }

    public static Comparator<Car> byModel() {
        return Comparator.comparing(Car::getModel);
    }
}
