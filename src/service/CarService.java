package service;

import model.Car;
import utils.CustomArrayList;

public interface CarService {
	
	CustomArrayList<String> getListNames() throws ServiceException;
	
	boolean saveAll(CustomArrayList<Car> carList, String nameList) throws ServiceException;
	
	boolean save(Car car, String nameList) throws ServiceException;
	
	boolean saveCarListNames(CustomArrayList<String> carListNames) throws ServiceException;

}
