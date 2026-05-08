package app;

import model.Car;
import repository.FileRepository;
import repository.Repository;
import repository.RepositoryException;
import sort.ArraySorted;
import sort.CarComparator;
import sort.SelectionSort;
import sort.SortStrategy;
import utils.CustomArrayList;

import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Repository repository = new FileRepository();
    private static CustomArrayList<Car> currentCars = new CustomArrayList<>();
    private static String currentFileName = "cars";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Программа сортировки автомобилей");

        while (true){
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
                    System.out.println("До свидания!");
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static void printMainMenu(){
        System.out.println("=== Главное меню ===");
        System.out.println("1. Заполнить данные");
        System.out.println("2. Сортировать автомобили");
        System.out.println("3. Показать все автомобили");
        System.out.println("4. Сохранить в файл");
        System.out.println("5. Выход");
    }

    private static int getIntInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
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

    }

    private static void loadFromFile() {
        System.out.print("Введите имя файла для загрузки: ");
        String fileName = scanner.nextLine();

        int size = getIntInput("Сколько автомобилей загрузить? (0 - загрузить все): ");

        if (size == 0) {
            size = Integer.MAX_VALUE;
        }

        try {
            currentCars = repository.readAll(size, fileName);
            if (currentCars.isEmpty()) {
                System.out.println("Файл пуст или не содержит корректных данных.");
            } else {
                currentFileName = fileName;
                System.out.println("Загружено " + currentCars.size() + " автомобилей.");
            }
        } catch (RepositoryException e) {
            System.out.println("Ошибка при загрузке: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        if (currentCars.isEmpty()) {
            System.out.println("Нет данных для сохранения! Сначала заполните данные.");
            return;
        }

        System.out.print("Введите имя файла для сохранения без расширения: ");
        String fileName = scanner.nextLine();

        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = currentFileName;
        }

        try {
            repository.save(currentCars, fileName);
            currentFileName = fileName;
            System.out.println("Данные успешно сохранены в файл '" + fileName + ".txt'!");
        } catch (RepositoryException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private static void displayCars() {
        if (currentCars.isEmpty()) {
            System.out.println("Список автомобилей пуст.");
            return;
        }

        System.out.println("=== Список автомобилей ===");
        for (int i = 0; i < currentCars.size(); i++) {
            System.out.println((i + 1) + ". " + currentCars.get(i));
        }
        System.out.println("Всего: " + currentCars.size() + " автомобилей.");
    }

    private static void sortCars() {
        if (currentCars.isEmpty()) {
            System.out.println("Нет данных для сортировки! Сначала заполните данные.");
            return;
        }

        System.out.println("=== Сортировка автомобилей ===");
        System.out.println("Выберите поле для сортировки:");
        System.out.println("1. По модели");
        System.out.println("2. По мощности");
        System.out.println("3. По году производства");

        int fieldChoice = getIntInput("Ваш выбор: ");

        Comparator<Car> comparator = null;
        switch (fieldChoice) {
            case 1:
                comparator = CarComparator.byModel();
                System.out.println("Сортировка по модели...");
                break;
            case 2:
                comparator = CarComparator.byHp();
                System.out.println("Сортировка по мощности...");
                break;
            case 3:
                comparator = CarComparator.byYear();
                System.out.println("Сортировка по году производства...");
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        SortStrategy sortStrategy = new SelectionSort();
        ArraySorted sorter = new ArraySorted(sortStrategy);
        sorter.sort(currentCars, comparator);

        System.out.println("Сортировка завершена!");
        displayCars();
    }

}
