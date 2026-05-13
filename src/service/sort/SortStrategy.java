package service.sort;

import java.util.Comparator;
import model.Car;
import utils.CustomArrayList;

public interface SortStrategy {

    void sort(CustomArrayList<Car> car, Comparator<Car> comparator);
}
