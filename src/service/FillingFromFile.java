package service;

import model.Car;
import repository.FileRepository;
import repository.Repository;
import repository.RepositoryException;
import utils.CustomArrayList;


public class FillingFromFile implements StrategyFilling{
	
	private final Repository repository = new FileRepository();

	@Override
	public CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException {
		
		try {
			CustomArrayList<Car> carList = repository.readAll(size, name);
			return carList;
		} catch (RepositoryException e) {
			throw new ServiceException("Не удалось найти информацию в файловой системе");
		}
	}
}
