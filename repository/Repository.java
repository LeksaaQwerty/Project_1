package repository;

import model.Car;
import utils.CustomArrayList;

public interface Repository {
	
	CustomArrayList<Car> readAll() throws RepositoryException;
	boolean save (CustomArrayList<Car> list) throws RepositoryException;
	boolean save (Car car) throws RepositoryException;

}
