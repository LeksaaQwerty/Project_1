package app;

import model.Car;
import service.FillingHands;
import service.ServiceException;
import service.StrategyFilling;
import utils.CustomArrayList;

public class Main {


    public static void main(String[] args) {
        

        // CustomArrayList<Car> carList = new CustomArrayList<>();
        // ArraySorted sorter = new ArraySorted(new SelectionSort());

        // carList.add(new Car.Builder("BMW M5 Competition").yearOfProduction(2020).hp(600).build());
        // carList.add(new Car.Builder("BMW M5 Com").yearOfProduction(2020).hp(625).build());
        // carList.add(new Car.Builder("Audi RS6").yearOfProduction(2022).hp(600).build());
        // carList.add(new Car.Builder("Mersedes-AMG C63 S").yearOfProduction(2014).hp(510).build());
        // carList.add(new Car.Builder("Tesla Model S").yearOfProduction(2023).hp(1020).build());


        StrategyFilling fillingStrategy = new FillingHands();

        try {
            System.out.println("--- Ручной ввод автомобилей ---");
            CustomArrayList<Car> cars = fillingStrategy.fillOut(2, "Мой Гараж");

            System.out.println("\nСписок успешно заполнен:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(cars.get(i));
            }

        } catch (ServiceException e) {
            System.err.println("Произошла ошибка при заполнении: " + e.getMessage());
        }
    }
}
