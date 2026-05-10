package service;

import java.util.Scanner;
import model.Car;
import utils.CustomArrayList;

public class FillingHands implements StrategyFilling {

    @Override
    public CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException {

        CustomArrayList<Car> carList = new CustomArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            for (int i = 0; i < size; i++) {
                System.out.println("Введите данные для " + (i + 1) + " автомобиля:");

                System.out.print("Модель: ");
                String model = scanner.nextLine();

                System.out.print("Мощность (л.с.): ");
                int hp = Integer.parseInt(scanner.nextLine());

                // 3. Читаем год производства
                System.out.print("Год производства: ");
                int year = Integer.parseInt(scanner.nextLine());

                Car car = new Car.Builder(model)
                        .hp(hp)
                        .yearOfProduction(year)
                        .build();

                carList.add(car);
            }

        } catch (NumberFormatException e) {
            throw new ServiceException("Введено не числовое значение!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ServiceException("Ошибка валидации " + e.getMessage());
        }

        return carList;
    }
}