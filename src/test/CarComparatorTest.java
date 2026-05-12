package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.sort.CarComparator;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты CarComparator")
class CarComparatorTest {

    private static final Car CAR1 = new Car.Builder("BMW")
            .yearOfProduction(2022)
            .hp(300)
            .build();

    private static final Car CAR2 = new Car.Builder("Audi")
            .yearOfProduction(2023)
            .hp(250)
            .build();

    private static final Car CAR3 = new Car.Builder("Tesla")
            .yearOfProduction(2024)
            .hp(450)
            .build();

    @Nested
    @DisplayName("Тесты компаратора byHp()")
    class ByHpTests {

        @Test
        @DisplayName("byHp() должен сравнивать автомобили по мощности")
        void byHp_ShouldCompareByHorsepower() {
            Comparator<Car> comparator = CarComparator.byHp();

            // CAR2 (250) < CAR1 (300) < CAR3 (450)
            assertTrue(comparator.compare(CAR2, CAR1) < 0);
            assertTrue(comparator.compare(CAR1, CAR3) < 0);
            assertTrue(comparator.compare(CAR3, CAR2) > 0);
        }

        @Test
        @DisplayName("byHp() должен возвращать 0 при одинаковой мощности")
        void byHp_WithSameHp_ShouldReturnZero() {
            Car carA = new Car.Builder("Toyota").yearOfProduction(2022).hp(200).build();
            Car carB = new Car.Builder("Honda").yearOfProduction(2023).hp(200).build();

            Comparator<Car> comparator = CarComparator.byHp();

            assertEquals(0, comparator.compare(carA, carB));
        }

        @Test
        @DisplayName("byHp() должен корректно сравнивать с null значениями")
        void byHp_ShouldHandleNullValues() {
            Comparator<Car> comparator = CarComparator.byHp();

            assertThrows(NullPointerException.class, () -> comparator.compare(null, CAR1));
        }
    }

    @Nested
    @DisplayName("Тесты компаратора byYear()")
    class ByYearTests {

        @Test
        @DisplayName("byYear() должен сравнивать автомобили по году выпуска")
        void byYear_ShouldCompareByYear() {
            Comparator<Car> comparator = CarComparator.byYear();

            // CAR1 (2022) < CAR2 (2023) < CAR3 (2024)
            assertTrue(comparator.compare(CAR1, CAR2) < 0);
            assertTrue(comparator.compare(CAR2, CAR3) < 0);
            assertTrue(comparator.compare(CAR3, CAR1) > 0);
        }

        @Test
        @DisplayName("byYear() должен возвращать 0 при одинаковом годе")
        void byYear_WithSameYear_ShouldReturnZero() {
            Car carA = new Car.Builder("Toyota").yearOfProduction(2022).hp(200).build();
            Car carB = new Car.Builder("Honda").yearOfProduction(2022).hp(250).build();

            Comparator<Car> comparator = CarComparator.byYear();

            assertEquals(0, comparator.compare(carA, carB));
        }

        @Test
        @DisplayName("byYear() должен корректно сравнивать разные года")
        void byYear_ShouldCompareDifferentYears() {
            Car car2020 = new Car.Builder("Old").yearOfProduction(2020).hp(100).build();
            Car car2024 = new Car.Builder("New").yearOfProduction(2024).hp(100).build();

            Comparator<Car> comparator = CarComparator.byYear();

            assertTrue(comparator.compare(car2020, car2024) < 0);
            assertTrue(comparator.compare(car2024, car2020) > 0);
        }
    }

    @Nested
    @DisplayName("Тесты компаратора byModel()")
    class ByModelTests {

        @Test
        @DisplayName("byModel() должен сравнивать автомобили по модели")
        void byModel_ShouldCompareByModel() {
            Comparator<Car> comparator = CarComparator.byModel();

            // "Audi" < "BMW" < "Tesla" (лексикографически)
            assertTrue(comparator.compare(CAR2, CAR1) < 0);
            assertTrue(comparator.compare(CAR1, CAR3) < 0);
            assertTrue(comparator.compare(CAR3, CAR2) > 0);
        }

        @Test
        @DisplayName("byModel() должен возвращать 0 при одинаковой модели")
        void byModel_WithSameModel_ShouldReturnZero() {
            Car carA = new Car.Builder("Toyota").yearOfProduction(2022).hp(200).build();
            Car carB = new Car.Builder("Toyota").yearOfProduction(2023).hp(250).build();

            Comparator<Car> comparator = CarComparator.byModel();

            assertEquals(0, comparator.compare(carA, carB));
        }

        @Test
        @DisplayName("byModel() должен учитывать регистр")
        void byModel_ShouldBeCaseSensitive() {
            Car carLower = new Car.Builder("audi").yearOfProduction(2022).hp(200).build();
            Car carUpper = new Car.Builder("Audi").yearOfProduction(2023).hp(250).build();

            Comparator<Car> comparator = CarComparator.byModel();

            // Сравнение с учетом регистра: "Audi" < "audi"
            assertNotEquals(0, comparator.compare(carUpper, carLower));
        }
    }
}
