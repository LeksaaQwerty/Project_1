package sort;

import java.util.Comparator;
import model.Car;
import utils.CustomArrayList;


public class SelectionSort implements SortStrategy {


    @Override
    public void sort(CustomArrayList<Car> car, Comparator<Car> comparator) {

        for (int i = 0; i < car.size() - 1; i++) {
            int minIndx = i;
            for (int j = i + 1; j < car.size(); j++) {
                if (comparator.compare(car.get(j), car.get(minIndx)) < 0) {
                    minIndx = j;
                }
            }

            Car temp = car.get(i);
            car.set(i, car.get(minIndx));
            car.set(minIndx, temp);
        }
    }
}
