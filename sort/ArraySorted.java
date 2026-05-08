package sort;

import java.util.Comparator;
import model.Car;
import utils.CustomArrayList;

public class ArraySorted {

    private final SortStrategy sortStrategy;

    public ArraySorted(SortStrategy sortStrategy){
        this.sortStrategy = sortStrategy;
    }

    public void sort(CustomArrayList<Car> car, Comparator<Car> comparator){
        sortStrategy.sort(car, comparator);
    }
}
