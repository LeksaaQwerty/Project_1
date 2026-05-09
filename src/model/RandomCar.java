package model;

import utils.RandomInRange;

public class RandomCar {
  public static String[] CAR_NAMES = {
      "Toyota Camry",
      "Honda Civic",
      "Ford Mustang",
      "BMW X5",
      "Tesla Model 3",
      "Audi A4",
      "Chevrolet Malibu",
      "Nissan Altima",
      "Hyundai Elantra",
      "Kia Sportage"
  };

  public static Car build() {
    int randomYear = RandomInRange.randomInt(Car.MIN_YEAR_OF_PRODUCTION, Car.MAX_YEAR_OF_PRODUCTION);
    int randomHP = RandomInRange.randomInt(Car.MIN_HP, Car.MAX_HP);
    String randomModel = CAR_NAMES[RandomInRange.randomInt(0, CAR_NAMES.length - 1)];

    return new Car.Builder(randomModel).yearOfProduction(randomYear).hp(randomHP).build();
  }
}
