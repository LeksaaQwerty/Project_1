package service;

import model.Car;
import repository.FileRepository;
import repository.Repository;
import repository.RepositoryException;
import utils.CustomArrayList;

public class CarServiceImpl implements CarService {

	private final Repository repository = new FileRepository();

	@Override
	public boolean saveAll(CustomArrayList<Car> carList, String nameList) throws ServiceException {

		try {
			return repository.save(carList, nameList);
		} catch (RepositoryException e) {
			throw new ServiceException("Не удалось сохранить");
		}
	}

	@Override
	public boolean save(Car car, String nameList) throws ServiceException {

		try {
			return repository.save(car, nameList);
		} catch (RepositoryException e) {
			throw new ServiceException("Не удалось сохранить");
		}
	}

	@Override
	public CustomArrayList<String> getListNames() throws ServiceException {
		try {
			return repository.readListOfCarLists();
		} catch (RepositoryException e) {
			throw new ServiceException("Списки автомобилей не найдены");
		}
	}

	@Override
	public boolean saveCarListNames(CustomArrayList<String> carListNames) throws ServiceException {
		
		try {
			return repository.saveCarListNames(carListNames);
		} catch (RepositoryException e) {
			throw new ServiceException("Ошибка сохранения каталога");
		}
	}
}
