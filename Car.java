import java.util.Comparator;

public class Car implements Comparable<Car>{

    private final String model;
    private final Integer hp;
    private final Integer yearOfProduction;

    private Car (Car.CarBuilder builder){
        model = builder.model;
        hp = builder.hp;
        yearOfProduction = builder.yearOfProduction;
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
    public String toString() {
        return "Модель: " + model + ", мощность: " + hp + ", год производства: " + yearOfProduction + "\n";
    }

    @Override
    public int compareTo(Car other) {
        return Comparator.comparing(Car::getHp)
                .thenComparing(Car::getYearOfProduction).reversed()
                .thenComparing(Car::getModel)
                .compare(this, other);
    }

    public static class CarBuilder {

        private final String model;

        private Integer hp = null;
        private Integer yearOfProduction = null;

        public CarBuilder(String model, int hp, int yearOfProduction) {
            this.model = model;
            this.hp = hp;
            this.yearOfProduction = yearOfProduction;
        }

//        public CarBuilder hp(int hp) {
//            this.hp = hp;
//            return this;
//        }
//
//        public CarBuilder yearOfProduction(int yearOfProduction) {
//            this.yearOfProduction = yearOfProduction;
//            return this;
//        }

        public Car build() {
            return new Car(this);
        }
    }
}