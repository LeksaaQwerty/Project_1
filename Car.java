

public class Car{

    private final String model;
    private final Integer hp;
    private final Integer yearOfProduction;

    public Car(String model, Integer hp, Integer yearOfProduction){
        this.model = model;
        this.hp = hp;
        this.yearOfProduction = yearOfProduction;
    }
    

    @Override
    public String toString() {
        return "Модель: " + model + ", мощность: " + hp + ", год производства: " + yearOfProduction; 
    }

     public static class Builder {

        
        private final String model;
        
        private Integer hp = null;
        private Integer yearOfProduction = null;

        public Builder(String Model) {
            this.model = Model;
        }

        public Builder hp(int hp) {
            this.hp = hp;
            return this;
        }

        public Builder yearOfProduction(int yearOfProduction) {
            this.yearOfProduction = yearOfProduction;
            return this;
        }

        // public Car build() {
        //     return new Car(this);
        // }
    }

}