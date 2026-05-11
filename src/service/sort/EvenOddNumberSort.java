package service.sort;

import java.util.Comparator;
import java.util.function.Function;
import model.Car;
import utils.CustomArrayList;

public class EvenOddNumberSort implements SortStrategy {
  private final Function<Car, Integer> extractor;

  public EvenOddNumberSort(Function<Car, Integer> extractor) {
    this.extractor = extractor;
  }

  @Override
  public void sort(CustomArrayList<Car> car, Comparator<Car> comparator) {
    // Создаем новый список для результата
    CustomArrayList<Car> result = new CustomArrayList<>();

    // Сначала добавляем все четные элементы, отсортированные
    for (int i = 0; i < car.size(); i++) {
      Car currentCar = car.get(i);
      if (extractor.apply(currentCar) % 2 == 0) {
        result.add(currentCar);
      }
    }

    // Сортируем четные элементы
    for (int i = 0; i < result.size() - 1; i++) {
      int minIndex = i;
      for (int j = i + 1; j < result.size(); j++) {
        if (comparator.compare(result.get(j), result.get(minIndex)) < 0) {
          minIndex = j;
        }
      }
      Car temp = result.get(i);
      result.set(i, result.get(minIndex));
      result.set(minIndex, temp);
    }

    // Затем добавляем все нечетные элементы, отсортированные
    CustomArrayList<Car> odds = new CustomArrayList<>();
    for (int i = 0; i < car.size(); i++) {
      Car currentCar = car.get(i);
      if (extractor.apply(currentCar) % 2 != 0) {
        odds.add(currentCar);
      }
    }

    // Сортируем нечетные элементы
    for (int i = 0; i < odds.size() - 1; i++) {
      int minIndex = i;
      for (int j = i + 1; j < odds.size(); j++) {
        if (comparator.compare(odds.get(j), odds.get(minIndex)) < 0) {
          minIndex = j;
        }
      }
      Car temp = odds.get(i);
      odds.set(i, odds.get(minIndex));
      odds.set(minIndex, temp);
    }

    // Добавляем нечетные в результат
    for (int i = 0; i < odds.size(); i++) {
      result.add(odds.get(i));
    }

    // Копируем результат обратно в исходный список
    for (int i = 0; i < result.size(); i++) {
      car.set(i, result.get(i));
    }
  }
}
