package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.sort.EvenOddNumberSort;
import utils.CustomArrayList;

import java.util.Comparator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты EvenOddNumberSort")
class EvenOddNumberSortTest {

    private static final Car CAR1 = new Car.Builder("BMW")
            .yearOfProduction(2022)
            .hp(301)
            .build();

    private static final Car CAR2 = new Car.Builder("Audi")
            .yearOfProduction(2023)
            .hp(250)
            .build();

    private static final Car CAR3 = new Car.Builder("Tesla")
            .yearOfProduction(2024)
            .hp(300)
            .build();

    private static final Car CAR4 = new Car.Builder("Mercedes")
            .yearOfProduction(2021)
            .hp(200)
            .build();

    private static final Car CAR5 = new Car.Builder("Toyota")
            .yearOfProduction(2022)
            .hp(280)
            .build();

    @Nested
    @DisplayName("Тесты с разными компараторами")
    class DifferentComparatorsTests {

        @Test
        @DisplayName("sort() с компаратором по убыванию")
        void sort_WithDescendingComparator() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR2); // 250 (чет)
            cars.add(CAR3); // 300 (чет)
            cars.add(CAR4); // 200 (чет)

            Comparator<Car> comparator = Comparator.comparing(Car::getHp).reversed();

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals(300, cars.get(0).getHp());
            assertEquals(250, cars.get(1).getHp());
            assertEquals(200, cars.get(2).getHp());
        }

        @Test
        @DisplayName("sort() с компаратором по модели")
        void sort_WithModelComparator() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            Car carA = new Car.Builder("BMW").yearOfProduction(2022).hp(200).build();
            Car carB = new Car.Builder("Audi").yearOfProduction(2022).hp(200).build();
            cars.add(carA);
            cars.add(carB);

            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals("Audi", cars.get(0).getModel());
            assertEquals("BMW", cars.get(1).getModel());
        }
    }

    @Nested
    @DisplayName("Граничные тесты")
    class BoundaryTests {

        @Test
        @DisplayName("sort() с пустым списком")
        void sort_WithEmptyList_ShouldDoNothing() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When & Then
            assertDoesNotThrow(() -> sorter.sort(cars, comparator));
            assertEquals(0, cars.size());
        }

        @Test
        @DisplayName("sort() с одним элементом")
        void sort_WithSingleElement() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals(1, cars.size());
            assertEquals(301, cars.get(0).getHp());
        }
    }
}
