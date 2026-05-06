package app;

import model.Car;
import repository.FileRepository;
import repository.Repository;
import repository.RepositoryException;
import utils.CustomArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Repository repository = new FileRepository();
    private static CustomArrayList<Car> currentCars = new CustomArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Программа сортировки автомобилей \n");

        while (true){
            printMainMenu();
            int choice = getIntInput("Выберите действие: ");

            switch (choice) {
                case 1:
                    fillData();
                    break;
//                case 2:
//                    sortCars();
//                    break;
//                case 3:
//                    displayCars();
//                    break;
//                case 4:
//                    saveToFile();
//                    break;
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
        System.out.println("=== Заполнение данных ===");
        System.out.println("1. Случайные данные");
        System.out.println("2. Ввести вручную");
        System.out.println("3. Загрузить из файла");

        int choice = getIntInput("Выберите способ: ");
        int size = getIntInput("Введите количество автомобилей: ");

        if (size <= 0) {
            System.out.println("Количество должно быть больше 0!");
            return;
        }

//        DataFillStrategy strategy = null;
//
        switch (choice) {
            case 1:
//                strategy = new RandomFillStrategy();
                break;
            case 2:
//                strategy = new ManualFillStrategy(scanner);
                break;
            case 3:
                loadFromFile();
                return;
            default:
                System.out.println("Неверный выбор!");
                return;
        }
    }

    private static void loadFromFile() {
        try {
            currentCars = repository.readAll();
            if (currentCars.isEmpty()) {
                System.out.println("Файл пуст или не содержит корректных данных.");
            } else {
                System.out.println("Загружено " + currentCars.size() + " автомобилей.");
                displayCars();
            }
        } catch (RepositoryException e) {
            System.out.println("Ошибка при загрузке: " + e.getMessage());
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
        System.out.println("Всего: " + currentCars.size() + " автомобилей.");
    }


}
