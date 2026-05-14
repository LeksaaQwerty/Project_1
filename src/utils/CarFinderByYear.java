package utils;

import model.Car;

public class CarFinderByYear extends Thread {
  private int yearToFind;
  private CustomArrayList<Car> cars;
  private CustomArrayList<Car> res = new CustomArrayList<>();

  public CarFinderByYear(CustomArrayList<Car> cars, int year) {
    this.yearToFind = year;
    this.cars = cars;
  }

  @Override
  public void run() {
    for (Car c : cars) {
      if (c == null)
        break;
      if (c.getYearOfProduction() == yearToFind) {
        res.add(c);
      }
    }
  }

  public CustomArrayList<Car> getResult() {
    return res;
  }
}
