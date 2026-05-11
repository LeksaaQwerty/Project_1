package repository;

import model.Car;
import utils.CustomArrayList;

public interface Repository {

  CustomArrayList<Car> readAll(int size, String name) throws RepositoryException;
  boolean save (CustomArrayList<Car> list, String name) throws RepositoryException;
  boolean save (Car car, String name) throws RepositoryException;
  CustomArrayList<String> readListOfCarLists() throws RepositoryException;
  boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException;

}
