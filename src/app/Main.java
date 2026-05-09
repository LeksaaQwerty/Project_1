package app;

import java.util.Comparator;
import model.Car;
import model.RandomCar;
import sort.ArraySorted;
import sort.CarComparator;
import sort.SelectionSort;
import utils.CustomArrayList;

public class Main {

  public static void main(String[] args) {

    CustomArrayList<Car> carList = new CustomArrayList<>();
    ArraySorted sorter = new ArraySorted(new SelectionSort());

    carList.add(new Car.Builder("BMW M5 Competition").yearOfProduction(2020).hp(600).build());
    carList.add(new Car.Builder("BMW M5 Com").yearOfProduction(2020).hp(625).build());
    carList.add(new Car.Builder("Audi RS6").yearOfProduction(2022).hp(600).build());
    carList.add(new Car.Builder("Mersedes-AMG C63 S").yearOfProduction(2014).hp(510).build());
    carList.add(new Car.Builder("Tesla Model S").yearOfProduction(2023).hp(1020).build());
    carList.add(RandomCar.build());

    System.out.println(carList);

    Comparator<Car> multiComparator = CarComparator.byModel()
        .thenComparing(CarComparator.byYear())
        .thenComparing(CarComparator.byHp());

    sorter.sort(carList, multiComparator);

    System.out.println(carList);
  }
}
