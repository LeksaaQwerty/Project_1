package model;

import java.util.Scanner;

public class ManualCar {
  private static final Scanner scanner = new Scanner(System.in);

  public static Car build() {
    System.out.println("\n=== Ввод данных автомобиля ===");

    // Ввод модели
    String model;
    while (true) {
      System.out.print("Введите модель автомобиля: ");
      model = scanner.nextLine().trim();
      if (model != null && !model.isEmpty() && !model.isBlank()) {
        break;
      }
      System.out.println("Ошибка: Модель не может быть пустой! Попробуйте снова.");
    }

    // Ввод мощности
    Integer hp = null;
    while (hp == null) {
      try {
        System.out.print("Введите мощность (от " + (Car.MIN_HP + 1) + " до " + Car.MAX_HP + " л.с.): ");
        int inputHp = Integer.parseInt(scanner.nextLine());
        if (inputHp > Car.MIN_HP && inputHp <= Car.MAX_HP) {
          hp = inputHp;
        } else {
          System.out.println("Ошибка: Мощность должна быть от " + (Car.MIN_HP + 1) + " до " + Car.MAX_HP + "!");
        }
      } catch (NumberFormatException e) {
        System.out.println("Ошибка: Пожалуйста, введите целое число!");
      }
    }

    // Ввод года производства
    Integer year = null;
    while (year == null) {
      try {
        System.out.print("Введите год производства (от " + Car.MIN_YEAR_OF_PRODUCTION + " до " + Car.MAX_YEAR_OF_PRODUCTION + "): ");
        int inputYear = Integer.parseInt(scanner.nextLine());
        if (inputYear >= Car.MIN_YEAR_OF_PRODUCTION && inputYear <= Car.MAX_YEAR_OF_PRODUCTION) {
          year = inputYear;
        } else {
          System.out.println("Ошибка: Год должен быть от " + Car.MIN_YEAR_OF_PRODUCTION + " до " + Car.MAX_YEAR_OF_PRODUCTION + "!");
        }
      } catch (NumberFormatException e) {
        System.out.println("Ошибка: Пожалуйста, введите целое число!");
      }
    }

    // Создание автомобиля через Builder
    try {
      return new Car.Builder(model)
        .hp(hp)
        .yearOfProduction(year)
        .build();
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("Ошибка при создании автомобиля: " + e.getMessage());
      return null;
    }
  }

  // Метод для создания нескольких автомобилей
  public static Car[] buildMultiple(int count) {
    Car[] cars = new Car[count];
    System.out.println("\n=== Ввод " + count + " автомобилей ===");

    for (int i = 0; i < count; i++) {
      System.out.println("\n--- Автомобиль " + (i + 1) + " из " + count + " ---");
      Car car = build();
      if (car != null) {
        cars[i] = car;
      } else {
        System.out.println("Ошибка при создании автомобиля " + (i + 1) + ". Попробуйте снова.");
        i--; // повторяем ввод для этого автомобиля
      }
    }

    return cars;
  }
}
