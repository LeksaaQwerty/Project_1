package test;
import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
public class CarTest {

    private static final String VALID_MODEL = "Toyota Camry";
    private static final int VALID_HP = 150;
    private static final int VALID_YEAR = 2022;

    @Nested
    @DisplayName("Тесты конструктора Builder")
    public class BuilderTests {

        @Test
        @DisplayName("Создание автомобиля с корректными параметрами")
        public void build_WithValidParameters_ShouldCreateCar() {
            // When
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertNotNull(car);
            assertEquals(VALID_MODEL, car.getModel());
            assertEquals(VALID_HP, car.getHp());
            assertEquals(VALID_YEAR, car.getYearOfProduction());
        }

        @Test
        @DisplayName("Выброс исключения, если модель null")
        public void builder_WithNullModel_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(null));
            assertEquals("Модель не может быть пустой или содержать только пробелы", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения, если модель пустая")
        public void builder_WithEmptyModel_ShouldThrowException() {
            // When & Then
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> new Car.Builder(""));
                assertEquals("Модель не может быть пустой или содержать только пробелы", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения, если модель состоит из пробелов")
        public void builder_WithBlankModel_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder("   "));
            assertEquals("Модель не может быть пустой или содержать только пробелы", exception.getMessage());
        }

        @Test
        @DisplayName("Установка мощности с валидным значением")
        public void hp_WithValidValue_ShouldSetHp() {
            // Given
            int validHp = 500;

            // When
            Car.Builder builder = new Car.Builder(VALID_MODEL).hp(validHp);

            // Then
            Car car = builder.yearOfProduction(VALID_YEAR).build();
            assertEquals(validHp, car.getHp());
        }

        @Test
        @DisplayName("Выброс исключения при мощности меньше MIN_HP")
        public void hp_WithHpLessThanMin_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).hp(Car.MIN_HP));
            assertEquals("Мощность должна быть больше 0 и не превышать 2000 л.с.", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения при мощности равной 0")
        public void hp_WithZeroHp_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).hp(0));
            assertEquals("Мощность должна быть больше 0 и не превышать 2000 л.с.", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения при отрицательной мощности")
        public void hp_WithNegativeHp_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).hp(-50));
            assertEquals("Мощность должна быть больше 0 и не превышать 2000 л.с.", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения при мощности больше MAX_HP")
        public void hp_WithHpGreaterThanMax_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).hp(Car.MAX_HP + 1));
            assertEquals("Мощность должна быть больше 0 и не превышать 2000 л.с.", exception.getMessage());
        }

        @Test
        @DisplayName("Установка года производства с валидным значением")
        public void yearOfProduction_WithValidYear_ShouldSetYear() {
            // Given
            int validYear = 2020;

            // When
            Car.Builder builder = new Car.Builder(VALID_MODEL).yearOfProduction(validYear);

            // Then
            Car car = builder.hp(VALID_HP).build();
            assertEquals(validYear, car.getYearOfProduction());
        }

        @Test
        @DisplayName("Выброс исключения при годе меньше MIN_YEAR_OF_PRODUCTION")
        public void yearOfProduction_WithYearLessThanMin_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).yearOfProduction(Car.MIN_YEAR_OF_PRODUCTION - 1));
            assertTrue(exception.getMessage().contains("Год производства должен быть больше"));
        }

        @Test
        @DisplayName("Выброс исключения при годе больше MAX_YEAR_OF_PRODUCTION")
        public void yearOfProduction_WithYearGreaterThanMax_ShouldThrowException() {
            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Car.Builder(VALID_MODEL).yearOfProduction(Car.MAX_YEAR_OF_PRODUCTION + 1));
            assertTrue(exception.getMessage().contains("Год производства должен быть больше"));
        }
        @Test
        @DisplayName("Выброс исключения при вызове build() без установки hp")
        public void build_WithoutHp_ShouldThrowException() {
            // Given
            Car.Builder builder = new Car.Builder(VALID_MODEL)
                    .yearOfProduction(VALID_YEAR);

            // When & Then
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    builder::build);
            assertEquals("Мощность и год производства должны быть установлены", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения при вызове build() без установки yearOfProduction")
        public void build_WithoutYear_ShouldThrowException() {
            // Given
            Car.Builder builder = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP);

            // When & Then
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    builder::build);
            assertEquals("Мощность и год производства должны быть установлены", exception.getMessage());
        }

        @Test
        @DisplayName("Выброс исключения при вызове build() без установки обоих полей")
        public void build_WithoutHpAndYear_ShouldThrowException() {
            // Given
            Car.Builder builder = new Car.Builder(VALID_MODEL);

            // When & Then
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    builder::build);
            assertEquals("Мощность и год производства должны быть установлены", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Тесты методов getter")
    public class GetterTests {

        private Car car;

        @BeforeEach
        public void setUp() {
            car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();
        }

        @Test
        @DisplayName("getModel() должен возвращать правильную модель")
        public void getModel_ShouldReturnCorrectModel() {
            assertEquals(VALID_MODEL, car.getModel());
        }

        @Test
        @DisplayName("getHp() должен возвращать правильную мощность")
        public void getHp_ShouldReturnCorrectHp() {
            assertEquals(VALID_HP, car.getHp());
        }

        @Test
        @DisplayName("getYearOfProduction() должен возвращать правильный год")
        public void getYearOfProduction_ShouldReturnCorrectYear() {
            assertEquals(VALID_YEAR, car.getYearOfProduction());
        }
    }

    @Nested
    @DisplayName("Тесты метода equals() и hashCode()")
    public class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Два автомобиля с одинаковыми параметрами должны быть равны")
        public void equals_SameParameters_ShouldReturnTrue() {
            // Given
            Car car1 = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            Car car2 = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertEquals(car1, car2);
            assertEquals(car1.hashCode(), car2.hashCode());
        }

        @Test
        @DisplayName("Два автомобиля с разными моделями не должны быть равны")
        public void equals_DifferentModels_ShouldReturnFalse() {
            // Given
            Car car1 = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            Car car2 = new Car.Builder("Honda Civic")
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertNotEquals(car1, car2);
        }

        @Test
        @DisplayName("Два автомобиля с разной мощностью не должны быть равны")
        public void equals_DifferentHp_ShouldReturnFalse() {
            // Given
            Car car1 = new Car.Builder(VALID_MODEL)
                    .hp(150)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            Car car2 = new Car.Builder(VALID_MODEL)
                    .hp(200)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertNotEquals(car1, car2);
        }

        @Test
        @DisplayName("Два автомобиля с разными годами не должны быть равны")
        public void equals_DifferentYears_ShouldReturnFalse() {
            // Given
            Car car1 = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(2020)
                    .build();

            Car car2 = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(2021)
                    .build();

            // Then
            assertNotEquals(car1, car2);
        }

        @Test
        @DisplayName("Сравнение с null должно возвращать false")
        public void equals_WithNull_ShouldReturnFalse() {
            // Given
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertNotEquals(null, car);
        }

        @Test
        @DisplayName("Сравнение с самим собой должно возвращать true")
        public void equals_WithItself_ShouldReturnTrue() {
            // Given
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertEquals(car, car);
        }

        @Test
        @DisplayName("Сравнение с объектом другого класса должно возвращать false")
        public void equals_WithDifferentClass_ShouldReturnFalse() {
            // Given
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertNotEquals(car, "Not a car");
        }
    }

    @Nested
    @DisplayName("Тесты метода toString()")
    public class ToStringTests {

        @Test
        @DisplayName("toString() должен содержать все поля")
        public void toString_ShouldContainAllFields() {
            // Given
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // When
            String result = car.toString();

            // Then
            assertTrue(result.contains(VALID_MODEL));
            assertTrue(result.contains(String.valueOf(VALID_HP)));
            assertTrue(result.contains(String.valueOf(VALID_YEAR)));
        }

        @Test
        @DisplayName("toString() должен иметь правильный формат")
        public void toString_ShouldHaveCorrectFormat() {
            // Given
            Car car = new Car.Builder("BMW X5")
                    .hp(300)
                    .yearOfProduction(2023)
                    .build();

            // When
            String result = car.toString();

            // Then
            assertEquals("Модель: BMW X5, мощность: 300, год производства: 2023", result);
        }
    }

    @Nested
    @DisplayName("Тесты граничных значений")
    public class BoundaryTests {

        @Test
        @DisplayName("Мощность равна MAX_HP должна быть валидной")
        public void hp_WithMaxValue_ShouldBeValid() {
            // When
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(Car.MAX_HP)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertEquals(Car.MAX_HP, car.getHp());
        }

        @Test
        @DisplayName("Мощность равна MIN_HP + 1 должна быть валидной")
        public void hp_WithMinPlusOneValue_ShouldBeValid() {
            // When
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(Car.MIN_HP + 1)
                    .yearOfProduction(VALID_YEAR)
                    .build();

            // Then
            assertEquals(Car.MIN_HP + 1, car.getHp());
        }

        @Test
        @DisplayName("Год равен MIN_YEAR_OF_PRODUCTION должен быть валидным")
        public void yearOfProduction_WithMinValue_ShouldBeValid() {
            // When
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(Car.MIN_YEAR_OF_PRODUCTION)
                    .build();

            // Then
            assertEquals(Car.MIN_YEAR_OF_PRODUCTION, car.getYearOfProduction());
        }

        @Test
        @DisplayName("Год равен MAX_YEAR_OF_PRODUCTION должен быть валидным")
        public void yearOfProduction_WithMaxValue_ShouldBeValid() {
            // When
            Car car = new Car.Builder(VALID_MODEL)
                    .hp(VALID_HP)
                    .yearOfProduction(Car.MAX_YEAR_OF_PRODUCTION)
                    .build();

            // Then
            assertEquals(Car.MAX_YEAR_OF_PRODUCTION, car.getYearOfProduction());
        }
    }

    @Nested
    @DisplayName("Тесты констант класса")
    public class ConstantsTests {

        @Test
        @DisplayName("MAX_HP должен быть равен 2000")
        public void maxHp_ShouldBe2000() {
            assertEquals(2000, Car.MAX_HP);
        }

        @Test
        @DisplayName("MIN_HP должен быть равен 0")
        public void minHp_ShouldBe0() {
            assertEquals(0, Car.MIN_HP);
        }

        @Test
        @DisplayName("MIN_YEAR_OF_PRODUCTION должен быть равен 1886")
        public void minYear_ShouldBe1886() {
            assertEquals(1886, Car.MIN_YEAR_OF_PRODUCTION);
        }

        @Test
        @DisplayName("MAX_YEAR_OF_PRODUCTION должен быть равен текущему году")
        public void maxYear_ShouldBeCurrentYear() {
            assertEquals(LocalDate.now().getYear(), Car.MAX_YEAR_OF_PRODUCTION);
        }
    }
}
