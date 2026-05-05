package service;

import model.Car;
import utils.CustomArrayList;

public interface StrategyFilling {
	
	CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException;

}
