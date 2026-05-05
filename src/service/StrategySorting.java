package service;

import model.Car;
import utils.CustomArrayList;

public interface StrategySorting {
	
	CustomArrayList<Car> sort(CustomArrayList<Car> list);

}
