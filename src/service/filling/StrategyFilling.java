package service.filling;

import model.Car;
import service.ServiceException;
import utils.CustomArrayList;

public interface StrategyFilling {
	
	CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException;

}
