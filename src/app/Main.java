package app;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import model.Car;
import service.CarService;
import service.CarServiceImpl;
import service.FillingFromFile;
import service.FillingManual;
import service.FillingRandom;
import service.ServiceException;
import service.SortByEvenPower;
import service.SortByEvenYear;
import service.SortByModel;
import service.SortByPower;
import service.SortByYear;
import service.StrategyFilling;
import service.StrategySorting;
import utils.CustomArrayList;

public class Main {

	private static CustomArrayList<Car> carList = new CustomArrayList<Car>(); // активный список авто
	private static String nameCarList; // имя активного списка авто
	private static Map<Integer, StrategyFilling> strategyFilling = new HashMap<>(); // методы заполнения активного
																					// списка
	private static Map<Integer, StrategySorting> strategySorting = new HashMap<>(); // методы сортировки
	private static final CarService service = new CarServiceImpl();
	private static CustomArrayList<String> listNames; // каталог списков авто

	static {
		strategyFilling.put(1, new FillingFromFile());
		strategyFilling.put(2, new FillingRandom());
		strategyFilling.put(3, new FillingManual());
	}
	static {
		strategySorting.put(1, new SortByModel());
		strategySorting.put(2, new SortByYear());
		strategySorting.put(3, new SortByPower());
		strategySorting.put(12, new SortByEvenYear());
		strategySorting.put(23, new SortByEvenPower());
	}
	static {
		try {
			listNames = service.getListNames();
		} catch (ServiceException e) {
			listNames = new CustomArrayList<String>();
		}
	}

	public static void main(String[] args) {

		while (true) {
			if (carList.isEmpty()) { // если текущий список пустой - заполняем
				fillingCarList();
				continue;
			}

			System.out.println("В базе данные есть автомобили. Выберите дествие >>>> ");
			System.out.println(
					" 1. Вывести на экран \n 2. Добавить один автомобиль \n 3. Произвести сортировку \n 4. Сохранить текущий список \n 5. Выбрать другой список");
			System.out.println("6. Закончить работу");

			int input = UserDataResponse.getIntAnswerFromUser(1, 6, "Выберите действие >>>>>  ");

			if (input == 6) {
				return;
			}

			if (input == 1) {
				System.out.println(carList.toString());
			}
			if (input == 2) {
				addCar();
			}
			if (input == 3) {
				sortCars();
			}
			if (input == 4) {
				saveCarList();
			}
			if (input == 5) {
				fillingCarList();
			}
		}
	}

	/*
	 * метод по выбору пользователя запускает разные методы заполнения активного
	 * списка
	 */
	private static void fillingCarList() {

		while (true) {

			System.out.println("База авто пуста. Выберите метод заполнения");
			System.out.println(" 1. Загрузить из файла \n 2. Заполнить в случайном порядке  \n 3. Создать вручную");
			System.out.println("4. Закончить работу");

			int input = UserDataResponse.getIntAnswerFromUser(1, 4, "Выберите действие >>>>>  ");

			if (input == 4) {
				return;
			}
			if (input == 1) {
				chooseNameCarList();
			}
			if (input == 2 && input == 3) {
				String name = UserDataResponse.getStringAnswerFromUser("Введите имя для создаваемого списка");
				if (listNames.contains(name)) {
					System.out.println("Такой список уже существует. Попробуйте снова");
					continue;
				}
				nameCarList = name;
			}
			if (nameCarList == null) {
				System.out.println("Не удалось создать имя для списка. Попробуйтае заново");
				continue;
			}
			StrategyFilling filling = strategyFilling.get(input);

			try {
				carList = filling.fillOut(
						UserDataResponse.getIntAnswerFromUser(1, 100, "Сколько автомобилей добавить в список? >>>>>  "),
						nameCarList);
			} catch (ServiceException e) {
				System.out.println("Загрузка из файла не удалась. Необходимо использовать другой способ.");
				continue;
			}
			return;
		}
	}

	/*
	 * метод реализует выбор пользователем списка авто из каталога
	 */
	private static void chooseNameCarList() {
		System.out.println("Выберите номер списка автомобилей из представленных ниже");
		int size = listNames.size();
		for (int i = 0; i < size; i++) {
			System.out.println(i + 1 + ". " + listNames.get(i));
		}
		System.out.println((size + 1) + ". Выйти в главное меню");

		int input = UserDataResponse.getIntAnswerFromUser(1, size + 1, "Выберите список >>>>>  ");

		if (input == size + 1) {
			return;
		}
		nameCarList = listNames.get(input - 1);
	}

	/*
	 * метод реализует сохранение активного списка авто
	 */
	private static void saveCarList() {

		if (listNames.contains(nameCarList)) {
			System.out.println(
					"Сохранение перезапишет существующий список.  \n 1.Сохранить под другим именем  \n 2. Сохранить с перезаписью \n 3.Вернуться в главное меню");
		}
		int input = UserDataResponse.getIntAnswerFromUser(1, 3, "Выберите вариант сохранения >>>>>  ");
		if (input == 3) {
			return;
		}
		if (input == 1) {
			String name = UserDataResponse.getStringAnswerFromUser("Введите имя для создаваемого списка");
			if (listNames.contains(name)) {
				System.out.println("Такой список уже существует. Попробуйте снова");
				return;
			}
			nameCarList = name;
			listNames.add(name);
		}
		try {
			service.saveAll(carList, nameCarList);
			service.saveCarListNames(listNames);
			System.out.println("Сохранение Прошло успешно");
		} catch (ServiceException e) {
			System.out.println("Сохранение не удалось.");
		}
	}

	/*
	 * метод реализует добавление одного авто в текущий список
	 */
	private static void addCar() {

		System.out.println("Создаём автомобиль(а зачем...)!");

		String model = UserDataResponse.getStringAnswerFromUser("Введите модель автомобиля");
		int year = UserDataResponse.getIntAnswerFromUser(1886, LocalDate.now().getYear(),
				"Введите год производства >>>>>");
		int power = UserDataResponse.getIntAnswerFromUser(1, 2000, "Введите мощность автомобиля >>>>>");
		Car car = new Car.Builder(model).yearOfProduction(year).hp(power).build();
		try {
			service.save(car, nameCarList);
			carList.add(car);
			System.out.println("Автомобиль добавлен");
		} catch (ServiceException e) {
			System.out.println("Автомобиль не удалось добавить");
		}
	}

	/*
	 * метод реализует выбор и запуск сортировки активного списка
	 */
	private static void sortCars() {

		while (true) {

			System.out.println(
					"Для сортировки доступны следующие показатели  \n 1. Модель \n 2. Год выпуска  \n 3. Мощность");
			System.out.println("4. Выйти в главное меню");

			int input = UserDataResponse.getIntAnswerFromUser(1, 4, "Выберите показатель для сортировки >>>>>");

			if (input == 4) {
				return;
			}
			int secondInput = 0;
			if (input == 2 || input == 3) {
				System.out.println(
						" Доступны следующие методы сортировки \n 1. Обычный по возрастанию \n 2. Сортировка по возрастаню только чётных значений");
				System.out.println("3. Выйти в главное меню");
				secondInput = UserDataResponse.getIntAnswerFromUser(1, 3, "Выберите метод сортировки >>>>>");
			}
			StrategySorting sorting = strategySorting.get(input + secondInput * 10);
			sorting.sort(carList);
			return;
		}
	}
}
