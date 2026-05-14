package app;

import model.Car;
import service.CarServiceImpl;
import service.ServiceException;
import service.filling.FillingFromFile;
import service.filling.FillingManual;
import service.filling.FillingRandom;
import service.filling.StrategyFilling;
import service.sort.ArraySorted;
import service.sort.CarComparator;
import service.sort.EvenOddNumberSort;
import service.sort.SelectionSort;
import service.sort.SortStrategy;
import utils.CarFinderByHp;
import utils.CarFinderByModel;
import utils.CarFinderByYear;
import utils.CustomArrayList;

import java.util.Comparator;
import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);
  private static CustomArrayList<Car> currentCars = new CustomArrayList<>();
  private static String currentFileName = "cars";
  private static final CarServiceImpl carService = new CarServiceImpl();

  public static void main(String[] args) {
    System.out.println("Программа сортировки автомобилей");

    while (true) {
      printMainMenu();
      int choice = getIntInput("Выберите действие: ");

      switch (choice) {
        case 1:
          fillData();
          break;
        case 2:
          sortCars();
          break;
        case 3:
          displayCars();
          break;
        case 4:
          saveToFile();
          break;
        case 5:
          searchByParam();
          break;
        case 6:
          System.out.println("До свидания!");
          System.exit(0);
        default:
          System.out.println("Неверный выбор! Попробуйте снова.");
      }
    }
  }

  private static void printMainMenu() {
    System.out.println("\n=== Главное меню ===");
    System.out.println("1. Заполнить данные");
    System.out.println("2. Сортировать автомобили");
    System.out.println("3. Показать все автомобили");
    System.out.println("4. Сохранить в файл");
    System.out.println("5. Поиск по параметрам");
    System.out.println("6. Выход");
  }

  private static int getIntInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      try {
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Пожалуйста, введите число!");
      }
    }
  }

  private static void fillData() {
    System.out.println("\n=== Заполнение данных ===");
    System.out.println("1. Случайные данные");
    System.out.println("2. Ввести вручную");
    System.out.println("3. Загрузить из файла");

    int choice = getIntInput("Выберите способ: ");

    if (choice == 3) {
      loadFromFile();
      return;
    }

    int size = getIntInput("Введите количество автомобилей: ");

    if (size <= 0) {
      System.out.println("Количество должно быть больше 0!");
      return;
    }

    StrategyFilling fillingStrategy = null;

    switch (choice) {
      case 1:
        fillingStrategy = new FillingRandom();
        System.out.println("Генерация случайных автомобилей...");
        break;
      case 2:
        fillingStrategy = new FillingManual();
        System.out.println("Ручной ввод автомобилей...");
        break;
      default:
        System.out.println("Неверный выбор!");
        return;
    }

    try {
      currentCars = fillingStrategy.fillOut(size, null);
      System.out.println("Успешно создано " + currentCars.size() + " автомобилей!");
      displayCars();
    } catch (ServiceException e) {
      System.out.println("Ошибка при заполнении: " + e.getMessage());
    }
  }

  private static String selectFileFromList(String action) {
    try {
      CustomArrayList<String> files = carService.getCarFilesList();

      if (files.isEmpty()) {
        System.out.println("Нет доступных файлов для " + action + "!");
        return null;
      }

      System.out.println("\nДоступные файлы:");
      for (int i = 0; i < files.size(); i++) {
        System.out.println((i + 1) + ". " + files.get(i));
      }

      int choice = getIntInput("Выберите файл для " + action + " (0 - отмена): ");

      if (choice == 0) {
        return null;
      }

      if (choice >= 1 && choice <= files.size()) {
        return files.get(choice - 1);
      }

      System.out.println("Неверный выбор!");
      return null;

    } catch (ServiceException e) {
      System.out.println("Ошибка получения списка файлов: " + e.getMessage());
      return null;
    }
  }

  private static void loadFromFile() {
    String fileName = selectFileFromList("загрузки");

    if (fileName == null) {
      System.out.println("Загрузка отменена.");
      return;
    }

    int size = getIntInput("Сколько автомобилей загрузить? (0 - загрузить все): ");

    if (size < 0) {
      System.out.println("Количество не может быть отрицательным!");
      return;
    }

    StrategyFilling fillingStrategy = new FillingFromFile();

    try {
      int limit = (size == 0) ? Integer.MAX_VALUE : size;
      CustomArrayList<Car> loadedCars = fillingStrategy.fillOut(limit, fileName);

      if (loadedCars.isEmpty()) {
        System.out.println("Файл пуст или не содержит корректных данных.");
      } else {
        currentCars = loadedCars;
        currentFileName = fileName;
        System.out.println("Загружено " + currentCars.size() + " автомобилей.");
        displayCars();
      }
    } catch (ServiceException e) {
      System.out.println("Ошибка при загрузке: " + e.getMessage());
    }
  }

  private static void saveToFile() {
    if (currentCars.isEmpty()) {
      System.out.println("Нет данных для сохранения! Сначала заполните данные.");
      return;
    }

    System.out.println("\n=== Сохранение данных ===");
    System.out.println("1. Сохранить в существующий файл");
    System.out.println("2. Сохранить в новый файл");

    int choice = getIntInput("Ваш выбор: ");

    String fileName;

    if (choice == 1) {
      fileName = selectFileFromList("сохранения");
    } else {
      System.out.print("Введите имя нового файла: ");
      fileName = scanner.nextLine().trim();
      if (fileName.isEmpty()) {
        System.out.println("Имя файла не может быть пустым! Используем имя по умолчанию: cars");
        fileName = "cars";
      }
    }

    try {
      boolean success = carService.saveAll(currentCars, fileName);

      if (success) {
        currentFileName = fileName;
        System.out.println("Данные успешно сохранены в файл '" + fileName + ".txt'!");
      } else {
        System.out.println("Не удалось сохранить данные.");
      }
    } catch (ServiceException e) {
      System.out.println("Ошибка при сохранении: " + e.getMessage());
    }
  }

  private static void displayCars() {
    if (currentCars.isEmpty()) {
      System.out.println("Список автомобилей пуст.");
      return;
    }

    System.out.println("\n=== Список автомобилей ===");
    for (int i = 0; i < currentCars.size(); i++) {
      System.out.println((i + 1) + ". " + currentCars.get(i));
    }
    System.out.println("Всего: " + currentCars.size() + " автомобилей.\n");
  }

  private static void sortCars() {
    if (currentCars.isEmpty()) {
      System.out.println("Нет данных для сортировки! Сначала заполните данные.");
      return;
    }

    System.out.println("\n=== Сортировка автомобилей ===");
    System.out.println("Выберите поле для сортировки:");
    System.out.println("1. По модели");
    System.out.println("2. По мощности");
    System.out.println("3. По году производства");
    System.out.println("4. По мощности (чётные в начало)");
    System.out.println("5. По году (чётные в начало)");

    int fieldChoice = getIntInput("Ваш выбор: ");

    Comparator<Car> comparator = null;
    SortStrategy sortStrategy = null;

    switch (fieldChoice) {
      case 1:
        comparator = CarComparator.byModel();
        sortStrategy = new SelectionSort();
        System.out.println("Сортировка по модели (выбором)...");
        break;
      case 2:
        comparator = CarComparator.byHp();
        sortStrategy = new SelectionSort();
        System.out.println("Сортировка по мощности (выбором)...");
        break;
      case 3:
        comparator = CarComparator.byYear();
        sortStrategy = new SelectionSort();
        System.out.println("Сортировка по году производства (выбором)...");
        break;
      case 4:
        comparator = CarComparator.byHp();
        sortStrategy = new EvenOddNumberSort(Car::getHp);
        System.out.println("Сортировка по мощности (чётные значения в начало)...");
        break;
      case 5:
        comparator = CarComparator.byYear();
        sortStrategy = new EvenOddNumberSort(Car::getYearOfProduction);
        System.out.println("Сортировка по году производства (чётные значения в начало)...");
        break;
      default:
        System.out.println("Неверный выбор!");
        return;
    }

    ArraySorted sorter = new ArraySorted(sortStrategy);
    sorter.sort(currentCars, comparator);

    System.out.println("Сортировка завершена!");
    displayCars();
  }

  private static void searchByParam() {
    System.out.println("\n=== Поиск по параметрам ===");
    System.out.println("1. Название модели");
    System.out.println("2. Мощность");
    System.out.println("3. Год выпуска");

    int choice = 0;
    while (choice < 1 || choice > 3) {
      choice = getIntInput("Выберите способ: ");
    }

    switch (choice) {
      case 1:
        searchByParamModel();
        break;
      case 2:
        searchByParamHp();
        break;
      case 3:
        searchByParamYear();
        break;
    }
  }

  private static void searchByParamModel() {
    System.out.print("Введите название модели: ");
    String choice = scanner.nextLine();

    try {
      CarFinderByModel finder = new CarFinderByModel(currentCars, choice);
      finder.run();
      finder.join();

      CustomArrayList<Car> foundCars = finder.getResult();
      System.out.println("Найдено " + foundCars.size() + " авто: ");
      System.out.println(foundCars.toString());
    } catch (InterruptedException e) {
      System.out.println("Ошибка поиска!");
      return;
    }
  }

  private static void searchByParamHp() {
    int choice = -1;
    while (choice < Car.MIN_HP || choice > Car.MAX_HP) {
      choice = getIntInput("Введите мощность: ");
    }

    try {
      CarFinderByHp finder = new CarFinderByHp(currentCars, choice);
      finder.run();
      finder.join();

      CustomArrayList<Car> foundCars = finder.getResult();
      System.out.println("Найдено " + foundCars.size() + " авто: ");
      System.out.println(foundCars.toString());
    } catch (InterruptedException e) {
      System.out.println("Ошибка поиска!");
      return;
    }
  }

  private static void searchByParamYear() {
    int choice = -1;
    while (choice < Car.MIN_YEAR_OF_PRODUCTION || choice > Car.MAX_YEAR_OF_PRODUCTION) {
      choice = getIntInput("Введите год выпуска: ");
    }

    try {
      CarFinderByYear finder = new CarFinderByYear(currentCars, choice);
      finder.run();
      finder.join();

      CustomArrayList<Car> foundCars = finder.getResult();
      System.out.println("Найдено " + foundCars.size() + " авто: ");
      System.out.println(foundCars.toString());
    } catch (InterruptedException e) {
      System.out.println("Ошибка поиска!");
      return;
    }
  }
}
