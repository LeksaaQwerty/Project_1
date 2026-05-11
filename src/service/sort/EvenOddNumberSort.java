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

	/*
	 * как добавить в меню: 
	 * case 4: 
	 * comparator = CarComparator.byHp(); 
	 * sortStrategy = new EvenOddNumberSort(Car::getHp); 
	 * System.out.println("Чётные HP...");
	 * break;
	 * 
	 * case 5:
	 * comparator = CarComparator.byYear(); 
	 * sortStrategy = new EvenOddNumberSort(Car::getYearOfProduction);
	 * System.out.println("Чётные Year..."); break;
	 * 
	 * остальные стратегии тоже инициализировать в case
	 */
	@Override
	public void sort(CustomArrayList<Car> car, Comparator<Car> comparator) {

		CustomArrayList<Car> evens = new CustomArrayList<>();

		for (int i = 0; i < car.size(); i++) {
			if (extractor.apply(car.get(i)) % 2 == 0) {
				evens.add(car.get(i));
			}
		}

		for (int i = 0; i < evens.size() - 1; i++) {
			int min = i;
			for (int j = i + 1; j < evens.size(); j++) {
				if (comparator.compare(evens.get(j), evens.get(min)) < 0) {
					min = j;
				}
			}

			Car tmp = evens.get(i);
			evens.set(i, evens.get(min));
			evens.set(min, tmp);
		}

		int e = 0;
		for (int i = 0; i < car.size(); i++) {
			if (extractor.apply(car.get(i)) % 2 == 0) {
				car.set(i, evens.get(e++));
			}
		}
	}
}