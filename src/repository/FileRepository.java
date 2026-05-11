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
      if (!Files.exists(path)) {
        throw new RepositoryException("Файл '" + name + ".txt' не найден");
      }
      carsData = Files.readString(path);
    } catch (IOException e) {
      throw new RepositoryException("Ошибка чтения файла: " + e.getMessage());
    }
    return parseCarsFromFile(carsData, size);
  }

  @Override
  public boolean save(CustomArrayList<Car> list, String name) throws RepositoryException {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < list.size(); i++) {
      sb.append(carToString(list.get(i)));
      if (i < list.size() - 1) {
        sb.append("///");
      }
    }

    Path path = Path.of(PATH, name + ".txt");
    try {
      Files.createDirectories(Path.of(PATH));
      Files.writeString(path, sb.toString());
    } catch (IOException e) {
      throw new RepositoryException("Не удалось сохранить: " + e.getMessage());
    }
    return true;
  }

  @Override
  public boolean save(Car car, String name) throws RepositoryException {
    Path path = Path.of(PATH, name + ".txt");
    String carData = carToString(car);

    try {
      Files.createDirectories(Path.of(PATH));
      if (Files.exists(path) && Files.size(path) > 0) {
        String existingContent = Files.readString(path);
        if (!existingContent.isEmpty()) {
          Files.writeString(path, "///" + carData, StandardOpenOption.APPEND);
        } else {
          Files.writeString(path, carData);
        }
      } else {
        Files.writeString(path, carData);
      }
    } catch (IOException e) {
      throw new RepositoryException("Не удалось сохранить: " + e.getMessage());
    }
    return true;
  }

  @Override
  public CustomArrayList<String> readListOfCarLists() throws RepositoryException {
    Path path = Path.of(PATH, "ListOfCarLists.txt");
    if (!Files.exists(path)) {
      return new CustomArrayList<>();
    }

    String listData;
    try {
      listData = Files.readString(path);
    } catch (IOException e) {
      throw new RepositoryException("Файл данных не найден");
    }

    String[] listNames = parseNames(listData);
    CustomArrayList<String> arrayNames = new CustomArrayList<>();
    for (String s : listNames) {
      if (s != null && !s.isBlank() && !s.isEmpty()) {
        arrayNames.add(s);
      }
    }
    return arrayNames;
  }

  @Override
  public boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException {
    Path path = Path.of(PATH, "ListOfCarLists.txt");
    try {
      Files.createDirectories(Path.of(PATH));
      Files.writeString(path, listNamesToString(carListNames));
    } catch (IOException e) {
      throw new RepositoryException("Не удалось сохранить: " + e.getMessage());
    }
    return true;
  }

  private CustomArrayList<Car> parseCarsFromFile(String carsData, int size) {
    CustomArrayList<Car> carList = new CustomArrayList<>();

    if (carsData == null || carsData.trim().isEmpty()) {
      return carList;
    }

    String[] carStrings = carsData.split("///");
    int limit = Math.min(size, carStrings.length);

    for (int i = 0; i < limit; i++) {
      try {
        Car car = parseCar(carStrings[i]);
        carList.add(car);
      } catch (IllegalArgumentException e) {
        System.err.println("Пропущен некорректный автомобиль: " + e.getMessage());
      }
    }
    return carList;
  }

  private Car parseCar(String carData) {
    String[] data = carData.split("//");

    if (data.length < 3) {
      throw new IllegalArgumentException("Некорректный формат данных автомобиля");
    }

    String model = data[0];
    if (model == null || model.trim().isEmpty()) {
      throw new IllegalArgumentException("Модель не может быть пустой");
    }

    Integer year = parseInt(data[1]);
    if (year < Car.MIN_YEAR_OF_PRODUCTION || year > Car.MAX_YEAR_OF_PRODUCTION) {
      throw new IllegalArgumentException("Год производства вне допустимого диапазона");
    }

    Integer hp = parseInt(data[2]);
    if (hp <= Car.MIN_HP || hp > Car.MAX_HP) {
      throw new IllegalArgumentException("Мощность вне допустимого диапазона");
    }

    return new Car.Builder(model)
      .yearOfProduction(year)
      .hp(hp)
      .build();
  }

  private Integer parseInt(String string) {
    if (string == null || string.equals("null") || string.trim().isEmpty()) {
      throw new IllegalArgumentException("Некорректное числовое значение: " + string);
    }
    try {
      return Integer.parseInt(string.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Нечисловое значение: " + string);
    }
  }

  private String carToString(Car car) {
    return car.getModel() + "//" + car.getYearOfProduction() + "//" + car.getHp();
  }

  private String[] parseNames(String listNames) {
    if (listNames == null || listNames.isEmpty()) {
      return new String[0];
    }
    return listNames.split("///");
  }

  private String listNamesToString(CustomArrayList<String> arrayNames) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arrayNames.size(); i++) {
      if (i > 0) {
        sb.append("///");
      }
      sb.append(arrayNames.get(i));
    }
    return sb.toString();
  }
}
