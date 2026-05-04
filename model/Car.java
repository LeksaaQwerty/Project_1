package model;

import java.time.LocalDate;
import java.util.Objects;

public class Car {

	private final String model;
	private final Integer hp;
	private final Integer yearOfProduction;

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
			if (hp <= 0 || hp > 2000) {
				throw new IllegalArgumentException("Мощность должна быть больше 0 и не превышать 2000 л.с.");
			} else {
				this.hp = hp;
			}
			return this;
		}

		public Builder yearOfProduction(int yearOfProduction) {
			int currentYear = LocalDate.now().getYear();

			if (yearOfProduction < 1886 || yearOfProduction > currentYear) {
				throw new IllegalArgumentException(
						"Год производства должен быть больше 1886 и меньше текущего года (" + currentYear + ")");
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