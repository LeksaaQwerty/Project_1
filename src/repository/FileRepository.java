package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import model.Car;
import utils.CustomArrayList;

public class FileRepository implements Repository {

	private static final String PATH = "files";

	@Override
	public CustomArrayList<Car> readAll(int size, String name) throws RepositoryException {
		Path path = Path.of(PATH, name + ".txt");
		String carsData;
		try {
			carsData = Files.readString(path);
		} catch (IOException e) {
			throw new RepositoryException("Файл данных не найден");
		}
		CustomArrayList<Car> cars = parseCarsFromFile(carsData, size);
		return cars;
	}

	@Override
	public boolean save(CustomArrayList<Car> list, String name) throws RepositoryException {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			sb.append(carToString(list.get(i))).append("///");
		}

		Path path = Path.of(PATH, name + ".txt");
		try {
			Files.writeString(path, sb.toString());
		} catch (IOException e) {
			throw new RepositoryException("Не удалось сохранить");
		}

		return true;

	}

	@Override
	public boolean save(Car car, String name) throws RepositoryException {

		Path path = Path.of(PATH, name + ".txt");
		String carData = "///" + carToString(car);
		Path answer;
		try {
			answer = Files.writeString(path, carData, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RepositoryException("Не удалось сохранить");
		}
		if (answer != null) {
			return true;
		}
		return false;
	}

	@Override
	public CustomArrayList<String> readListOfCarLists() throws RepositoryException {
		Path path = Path.of(PATH, "ListOfCarLists.txt");
		String listData;
		try {
			listData = Files.readString(path);
		} catch (IOException e) {
			throw new RepositoryException("Файл данных не найден");
		}
		String[] listNames = parseNames(listData);
		CustomArrayList<String> arrayNames = new CustomArrayList<String>();
		for(String s : listNames) {
			if(s != null && !s.isBlank() && !s.isEmpty()) {
				arrayNames.add(s);
			}
		}
		return arrayNames;
	}
	
	@Override
	public boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException {
		Path path = Path.of(PATH, "ListOfCarLists.txt");
		try {
			Files.writeString(path, listNamesToString(carListNames));
		} catch (IOException e) {
			throw new RepositoryException("Не удалось сохранить");
		}
		return false;
	}


	private CustomArrayList<Car> parseCarsFromFile(String carsData, int size) {
		CustomArrayList<Car> carList = new CustomArrayList<Car>();
		if (carsData != null && !carsData.isEmpty()) {
			String[] cars = carsData.split("///");
			for (int i = 0; i < cars.length && carList.size() < size; i++) {
				Car car = parseCar(cars[i]);
				carList.add(car);
			}
		}
		return carList;
	}

	private Car parseCar(String carData) {

		String[] data = carData.split("//");

		Car car = new Car.Builder(data[0]).yearOfProduction(parseInt(data[1])).hp(parseInt(data[2])).build();

		return car;
	}

	private Integer parseInt(String string) {
		if (string == null || string.equals("null") || string.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(string);
	}

	private String carToString(Car car) {
		StringBuffer sb = new StringBuffer();
		sb.append(car.getModel()).append("//").append(car.getYearOfProduction()).append("//").append(car.getHp());
		return sb.toString();
	}

	private String[] parseNames(String listNames) {
		return listNames.split("///");
	}

	private String listNamesToString(CustomArrayList<String> arrayNames) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arrayNames.size(); i++) {
			sb.append("///").append(arrayNames.get(i));
		}
		return sb.toString();
	}

}
