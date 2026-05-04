package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import model.Car;
import utils.CustomArrayList;

public class FileRepository implements Repository {

	private static final String PATH = "files/cars.txt";

	@Override
	public CustomArrayList<Car> readAll() throws RepositoryException {
		Path path = Path.of(PATH);
		String carsData;
		try {
			carsData = Files.readString(path);
		} catch (IOException e) {
			throw new RepositoryException("Файл данных не найден");
		}
		CustomArrayList<Car> cars = parseCarsFromFile(carsData);
		return cars;
	}

	@Override
	public boolean save(CustomArrayList<Car> list) throws RepositoryException {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			sb.append(carToString(list.get(i))).append("///");
		}

		Path path = Path.of(PATH);
		try {
			Files.writeString(path, sb.toString());
		} catch (IOException e) {
			throw new RepositoryException("Не удалось сохранить");
		}

		return true;

	}

	@Override
	public boolean save(Car car) throws RepositoryException {

		Path path = Path.of(PATH);
		String carData = carToString(car);
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

	private CustomArrayList<Car> parseCarsFromFile(String carsData) {
		CustomArrayList<Car> carList = new CustomArrayList<Car>();
		if (carsData != null && !carsData.isEmpty()) {
			String[] cars = carsData.split("///");
			for (int i = 0; i < cars.length; i++) {
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

}
