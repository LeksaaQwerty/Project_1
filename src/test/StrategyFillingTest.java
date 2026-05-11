package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.ServiceException;
import service.filling.StrategyFilling;
import utils.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты интерфейса StrategyFilling")
class StrategyFillingTest {

    // Тестовая реализация интерфейса
    private static class TestStrategyFilling implements StrategyFilling {
        private final CustomArrayList<Car> cars;
        private final boolean shouldThrowException;

        TestStrategyFilling(CustomArrayList<Car> cars, boolean shouldThrowException) {
            this.cars = cars;
            this.shouldThrowException = shouldThrowException;
        }

        @Override
        public CustomArrayList<Car> fillOut(Integer size, String name) throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Тестовая ошибка");
            }

            CustomArrayList<Car> result = new CustomArrayList<>();
            int limit = Math.min(size, cars.size());
            for (int i = 0; i < limit; i++) {
                result.add(cars.get(i));
            }
            return result;
        }
    }

    private static final Car CAR1 = new Car.Builder("Toyota Camry")
            .yearOfProduction(2022)
            .hp(150)
            .build();

    private static final Car CAR2 = new Car.Builder("BMW X5")
            .yearOfProduction(2023)
            .hp(300)
            .build();

    @Nested
    @DisplayName("Тесты с успешной реализацией")
    class SuccessTests {

        @Test
        @DisplayName("fillOut() должен возвращать список автомобилей")
        void fillOut_ShouldReturnCarList() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            testCars.add(CAR2);

            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(10, "test");

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(CAR1, result.get(0));
            assertEquals(CAR2, result.get(1));
        }

        @Test
        @DisplayName("fillOut() должен ограничивать количество по параметру size")
        void fillOut_ShouldLimitBySize() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            testCars.add(CAR2);

            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(1, "test");

            // Then
            assertEquals(1, result.size());
            assertEquals(CAR1, result.get(0));
        }

        @Test
        @DisplayName("fillOut() с size = 0 должен возвращать пустой список")
        void fillOut_WithZeroSize_ShouldReturnEmptyList() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);

            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(0, "test");

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("fillOut() с пустым списком автомобилей должен возвращать пустой список")
        void fillOut_WithEmptyCarList_ShouldReturnEmptyList() throws ServiceException {
            // Given
            CustomArrayList<Car> emptyList = new CustomArrayList<>();
            StrategyFilling strategy = new TestStrategyFilling(emptyList, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(10, "test");

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("fillOut() должен игнорировать параметр name в данной реализации")
        void fillOut_ShouldIgnoreNameParameter() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);

            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result1 = strategy.fillOut(1, "name1");
            CustomArrayList<Car> result2 = strategy.fillOut(1, "name2");

            // Then
            assertEquals(result1.size(), result2.size());
            assertEquals(result1.get(0), result2.get(0));
        }
    }

    @Nested
    @DisplayName("Тесты с выбрасыванием исключения")
    class ExceptionTests {

        @Test
        @DisplayName("fillOut() должен выбрасывать ServiceException при ошибке")
        void fillOut_ShouldThrowServiceException() {
            // Given
            StrategyFilling strategy = new TestStrategyFilling(new CustomArrayList<>(), true);

            // When & Then
            assertThrows(ServiceException.class,
                    () -> strategy.fillOut(10, "test"));
        }

        @Test
        @DisplayName("Исключение должно содержать сообщение об ошибке")
        void exception_ShouldHaveMessage() {
            // Given
            StrategyFilling strategy = new TestStrategyFilling(new CustomArrayList<>(), true);

            // When
            ServiceException exception = assertThrows(ServiceException.class,
                    () -> strategy.fillOut(10, "test"));

            // Then
            assertEquals("Тестовая ошибка", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Тесты с разными типами данных")
    class DifferentDataTypesTests {

        @Test
        @DisplayName("fillOut() с null в качестве name не должен падать")
        void fillOut_WithNullName_ShouldNotCrash() {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When & Then
            assertDoesNotThrow(() -> {
                CustomArrayList<Car> result = strategy.fillOut(1, null);
                assertNotNull(result);
            });
        }

        @Test
        @DisplayName("fillOut() с пустым name должен работать")
        void fillOut_WithEmptyName_ShouldWork() {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When & Then
            assertDoesNotThrow(() -> {
                CustomArrayList<Car> result = strategy.fillOut(1, "");
                assertNotNull(result);
            });
        }

        @Test
        @DisplayName("fillOut() с отрицательным size может выбрасывать исключение")
        void fillOut_WithNegativeSize_MayThrowException() {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When & Then
            // Реализация может как выбросить исключение, так и вернуть пустой список
            try {
                CustomArrayList<Car> result = strategy.fillOut(-1, "test");
                assertNotNull(result);
            } catch (ServiceException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Тесты граничных значений")
    class BoundaryTests {

        @Test
        @DisplayName("fillOut() с максимальным size должен работать")
        void fillOut_WithMaxSize_ShouldWork() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            for (int i = 0; i < 1000; i++) {
                testCars.add(CAR1);
            }
            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(Integer.MAX_VALUE, "test");

            // Then
            assertNotNull(result);
            assertEquals(1000, result.size());
        }

        @Test
        @DisplayName("fillOut() с size больше чем доступно автомобилей")
        void fillOut_WithSizeGreaterThanAvailable() throws ServiceException {
            // Given
            CustomArrayList<Car> testCars = new CustomArrayList<>();
            testCars.add(CAR1);
            testCars.add(CAR2);
            StrategyFilling strategy = new TestStrategyFilling(testCars, false);

            // When
            CustomArrayList<Car> result = strategy.fillOut(100, "test");

            // Then
            assertEquals(2, result.size());
        }
    }
}