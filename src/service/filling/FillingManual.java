package service.filling;

import model.Car;
import model.ManualCar;
import service.ServiceException;
import utils.CustomArrayList;

public class FillingManual implements StrategyFilling {

  @Override
  public CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException {
    if (size == null || size <= 0) {
      throw new ServiceException("Размер должен быть больше 0");
    }

    CustomArrayList<Car> carList = new CustomArrayList<>();

    System.out.println("\n=== Ручной ввод " + size + " автомобилей ===");

    for (int i = 0; i < size; i++) {
      System.out.println("\n--- Автомобиль " + (i + 1) + " из " + size + " ---");
      Car car = ManualCar.build();
      if (car != null) {
        carList.add(car);
        System.out.println("✓ Автомобиль успешно добавлен!");
      } else {
        System.out.println("✗ Ошибка при создании автомобиля. Попробуйте снова.");
        i--; // повторяем ввод для этого автомобиля
      }
    }

    System.out.println("\n✓ Все " + carList.size() + " автомобилей успешно введены!");
    return carList;
  }
}
