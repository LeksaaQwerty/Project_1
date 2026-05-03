public class Car{

    private final String model;
    private final Integer hp;
    private final Integer yearOfProduction;


    public Car(Builder builder){
        this.model = builder.model;
        this.hp = builder.hp;
        this.yearOfProduction = builder.yearOfProduction;
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

        public Car build() {

            if(model==null || model.trim().isEmpty()) {
                throw new IllegalStateException("Модель не может быть пустой! " + model);
            }

            if(yearOfProduction != null && (yearOfProduction<1885 || yearOfProduction > 2026)){
                throw new IllegalArgumentException("Не корректный ввод года! " + yearOfProduction);
            }

            if(hp != null && hp<0){
                throw new IllegalArgumentException("Мощность машины введена не корректно! " + hp);
            }

            return new Car(this);
        }
    }

}