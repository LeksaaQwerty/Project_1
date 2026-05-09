package model;

import java.time.LocalDate;
import java.util.Objects;

public class Car {

  private final String model;
  private final Integer hp;
  private final Integer yearOfProduction;

  public static int MAX_HP = 2000;
  public static int MIN_HP = 0;

  public static int MAX_YEAR_OF_PRODUCTION = LocalDate.now().getYear();
  public static int MIN_YEAR_OF_PRODUCTION = 1886;

  private Car(Builder builder) {
    this.model = builder.model;
    this.hp = builder.hp;
    this.yearOfProduction = builder.yearOfProduction;
  }

  @Override
  public String toString() {
    return "Модель: " + model + ", мощность: " + hp + ", год производства: " + yearOfProduction;
  }

  public String getModel() {
    return model;
  }

  public Integer getHp() {
    return hp;
  }

  public Integer getYearOfProduction() {
    return yearOfProduction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(hp, model, yearOfProduction);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Car other = (Car) obj;
    return Objects.equals(hp, other.hp) && Objects.equals(model, other.model)
        && Objects.equals(yearOfProduction, other.yearOfProduction);
  }

  public static class Builder {

    private final String model;
    private Integer hp = null;
    private Integer yearOfProduction = null;

    public Builder(String model) {
      if (model == null || model.isEmpty() || model.isBlank()) {
        throw new IllegalArgumentException("Модель не может быть пустой или содержать только пробелы");
      } else {
        this.model = model;
      }
    }

    public Builder hp(int hp) {
      if (hp <= MIN_HP || hp > MAX_HP) {
        throw new IllegalArgumentException("Мощность должна быть больше 0 и не превышать 2000 л.с.");
      } else {
        this.hp = hp;
      }
      return this;
    }

    public Builder yearOfProduction(int yearOfProduction) {
      if (yearOfProduction < MIN_YEAR_OF_PRODUCTION || yearOfProduction > MAX_YEAR_OF_PRODUCTION) {
        throw new IllegalArgumentException(
            "Год производства должен быть больше " + MIN_YEAR_OF_PRODUCTION + " и меньше (" + MAX_YEAR_OF_PRODUCTION
                + ")");
      } else {
        this.yearOfProduction = yearOfProduction;
      }
      return this;
    }

    public Car build() {
      if (hp == null || yearOfProduction == null) {
        throw new IllegalStateException("Мощность и год производства должны быть установлены");
      }
      return new Car(this);
    }
  }
}
