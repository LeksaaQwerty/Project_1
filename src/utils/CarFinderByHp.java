package utils;

import model.Car;

public class CarFinderByHp extends Thread {
  private int hpToFind;
  private CustomArrayList<Car> cars;
  private CustomArrayList<Car> res = new CustomArrayList<>();

  public CarFinderByHp(CustomArrayList<Car> cars, int hp) {
    this.hpToFind = hp;
    this.cars = cars;
  }

  @Override
  public void run() {
    for (Car c : cars) {
      if (c == null)
        break;
      if (c.getHp() == hpToFind) {
        res.add(c);
      }
    }
  }

  public CustomArrayList<Car> getResult() {
    return res;
  }
}
