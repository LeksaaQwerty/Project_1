package service.filling;

import java.util.stream.Stream;

import model.Car;
import model.RandomCar;
import service.ServiceException;
import utils.CustomArrayList;

public class FillingRandom implements StrategyFilling {

	@Override
	public CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException {
		
		CustomArrayList<Car> carList = new CustomArrayList<Car>();
		Stream.generate(RandomCar::build).limit(size).forEach(carList::add);
		return carList;
	}
}
