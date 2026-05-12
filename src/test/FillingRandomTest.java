package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.ServiceException;
import service.filling.FillingRandom;
import utils.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса FillingRandom")
class FillingRandomTest {

    private FillingRandom fillingRandom;

    @BeforeEach
    void setUp() {
        fillingRandom = new FillingRandom();
    }

    @Nested
    @DisplayName("Позитивные тесты")
    class PositiveTests {

        @Test
        @DisplayName("fillOut() должен создавать указанное количество автомобилей")
        void fillOut_ShouldCreateSpecifiedNumberOfCars() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(5, "anyName");

            // Then
            assertNotNull(result);
            assertEquals(5, result.size());
        }

        @Test
        @DisplayName("fillOut() с size = 1 должен создать один автомобиль")
        void fillOut_WithSize1_ShouldCreateOneCar() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(1, "anyName");

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertNotNull(result.get(0));
        }

        @Test
        @DisplayName("fillOut() с size = 0 должен вернуть пустой список")
        void fillOut_WithZeroSize_ShouldReturnEmptyList() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(0, "anyName");

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("fillOut() с size = 100 должен создать 100 автомобилей")
        void fillOut_WithLargeSize_ShouldCreateManyCars() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(100, "anyName");

            // Then
            assertEquals(100, result.size());
        }

        @Test
        @DisplayName("fillOut() должен игнорировать параметр name")
        void fillOut_ShouldIgnoreNameParameter() throws ServiceException {
            // When
            CustomArrayList<Car> result1 = fillingRandom.fillOut(3, "name1");
            CustomArrayList<Car> result2 = fillingRandom.fillOut(3, "name2");

            // Then
            assertEquals(result1.size(), result2.size());
        }
    }

    @Nested
    @DisplayName("Тесты валидации данных")
    class DataValidationTests {

        @Test
        @DisplayName("Все созданные автомобили должны быть валидными")
        void allCars_ShouldBeValid() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(10, "anyName");

            // Then
            for (int i = 0; i < result.size(); i++) {
                Car car = result.get(i);
                assertNotNull(car.getModel());
                assertNotNull(car.getHp());
                assertNotNull(car.getYearOfProduction());
                assertTrue(car.getHp() > 0);
                assertTrue(car.getYearOfProduction() >= 1886);
            }
        }

        @Test
        @DisplayName("Автомобили могут иметь разные модели")
        void cars_ShouldHaveDifferentModels() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(20, "anyName");

            // Then
            boolean hasDifferentModels = false;
            String firstModel = result.get(0).getModel();
            for (int i = 1; i < result.size(); i++) {
                if (!result.get(i).getModel().equals(firstModel)) {
                    hasDifferentModels = true;
                    break;
                }
            }
            assertTrue(hasDifferentModels, "Должны быть автомобили с разными моделями");
        }

        @Test
        @DisplayName("Автомобили могут иметь разную мощность")
        void cars_ShouldHaveDifferentHorsepower() throws ServiceException {
            // When
            CustomArrayList<Car> result = fillingRandom.fillOut(20, "anyName");

            // Then
            boolean hasDifferentHp = false;
            Integer firstHp = result.get(0).getHp();
            for (int i = 1; i < result.size(); i++) {
                if (!result.get(i).getHp().equals(firstHp)) {
                    hasDifferentHp = true;
                    break;
                }
            }
            assertTrue(hasDifferentHp, "Должны быть автомобили с разной мощностью");
        }
    }

        @Test
        @DisplayName("fillOut() с null name должен работать (name игнорируется)")
        void fillOut_WithNullName_ShouldWork() throws ServiceException {
            // When & Then
            assertDoesNotThrow(() -> {
                CustomArrayList<Car> result = fillingRandom.fillOut(3, null);
                assertEquals(3, result.size());
            });
        }

        @Test
        @DisplayName("fillOut() с пустым name должен работать")
        void fillOut_WithEmptyName_ShouldWork() throws ServiceException {
            // When & Then
            assertDoesNotThrow(() -> {
                CustomArrayList<Car> result = fillingRandom.fillOut(3, "");
                assertEquals(3, result.size());
            });
        }
}
