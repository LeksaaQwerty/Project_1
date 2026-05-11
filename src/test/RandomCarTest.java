package test;
import model.Car;
import model.RandomCar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
public class RandomCarTest {

    @Test
    @DisplayName("Проверка, что build() возвращает не null")
    public void buildShouldReturnNotNull() {
        Car car = RandomCar.build();
        assertNotNull(car, "Метод build() не должен возвращать null");
    }

    @Test
    @DisplayName("Проверка, что build() возвращает разные автомобили при нескольких вызовах")
    public void buildShouldReturnDifferentCars() {
        Car car1 = RandomCar.build();
        Car car2 = RandomCar.build();

        // Автомобили могут быть одинаковыми, но вероятность мала
        // Проверяем, что это разные объекты
        assertNotSame(car1, car2, "Разные вызовы должны возвращать разные объекты");

        // Дополнительно выводим для наглядности
        System.out.println("Car 1: " + car1);
        System.out.println("Car 2: " + car2);
    }

    @RepeatedTest(20)
    @DisplayName("Проверка, что сгенерированный автомобиль имеет корректные параметры")
    public void buildShouldGenerateValidCar() {
        Car car = RandomCar.build();

        // Проверка модели
        String model = car.getModel(); // предполагаем, что есть геттер
        assertNotNull(model, "Модель не должна быть null");
        assertTrue(model.length() > 0, "Модель не должна быть пустой");

        // Проверка, что модель из списка допустимых
        boolean isValidModel = false;
        for (String validModel : RandomCar.CAR_NAMES) {
            if (validModel.equals(model)) {
                isValidModel = true;
                break;
            }
        }
        assertTrue(isValidModel, "Модель '" + model + "' не найдена в списке допустимых");

        // Проверка года выпуска
        int year = car.getYearOfProduction(); // предполагаем, что есть геттер
        assertTrue(year >= Car.MIN_YEAR_OF_PRODUCTION && year <= Car.MAX_YEAR_OF_PRODUCTION,
                "Год выпуска " + year + " вне допустимого диапазона ["
                        + Car.MIN_YEAR_OF_PRODUCTION + ", " + Car.MAX_YEAR_OF_PRODUCTION + "]");

        // Проверка лошадиных сил
        int hp = car.getHp(); // предполагаем, что есть геттер
        assertTrue(hp >= Car.MIN_HP && hp <= Car.MAX_HP,
                "Лошадиные силы " + hp + " вне допустимого диапазона ["
                        + Car.MIN_HP + ", " + Car.MAX_HP + "]");
    }

    @Test
    @DisplayName("Проверка, что массив CAR_NAMES не пустой и содержит все названия")
    public void carNamesArrayShouldBeValid() {
        assertNotNull(RandomCar.CAR_NAMES, "Массив CAR_NAMES не должен быть null");
        assertTrue(RandomCar.CAR_NAMES.length > 0, "Массив CAR_NAMES не должен быть пустым");

        for (String name : RandomCar.CAR_NAMES) {
            assertNotNull(name, "Название модели не должно быть null");
            assertFalse(name.trim().isEmpty(), "Название модели не должно быть пустым");
        }
    }

    @Test
    @DisplayName("Проверка уникальности названий моделей (опционально)")
    public void carNamesShouldBeUnique() {
        for (int i = 0; i < RandomCar.CAR_NAMES.length; i++) {
            for (int j = i + 1; j < RandomCar.CAR_NAMES.length; j++) {
                assertNotEquals(RandomCar.CAR_NAMES[i], RandomCar.CAR_NAMES[j],
                        "Названия моделей должны быть уникальными: " + RandomCar.CAR_NAMES[i]);
            }
        }
    }
}