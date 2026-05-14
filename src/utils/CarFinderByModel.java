package utils;

import model.Car;

public class CarFinderByModel extends Thread {
  private String modelToFind;
  private CustomArrayList<Car> cars;
  private CustomArrayList<Car> res = new CustomArrayList<>();

  public CarFinderByModel(CustomArrayList<Car> cars, String model) {
    this.modelToFind = model;
    this.cars = cars;
  }

  @Override
  public void run() {
    for (Car c : cars) {
      if (c == null)
        break;
      if (c.getModel().toLowerCase().contains(modelToFind)) {
        res.add(c);
      }
    }
  }

  public CustomArrayList<Car> getResult() {
    return res;
  }
}
