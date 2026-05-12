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
    @DisplayName("Тесты сортировки по мощности (hp)")
    class SortByHpTests {

        @Test
        @DisplayName("sort() должен сортировать только четные значения hp")
        void sort_ShouldSortOnlyEvenHpValues() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1); // hp=301 (нечет)
            cars.add(CAR2); // hp=250 (чет)
            cars.add(CAR3); // hp=300 (чет)
            cars.add(CAR4); // hp=200 (чет)

            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            sorter.sort(cars, comparator);

            // Then
            // Четные значения должны быть отсортированы: 200, 250, 300
            // Нечетные остаются на своих местах
            assertEquals(301, cars.get(0).getHp()); // Нечет не меняется
            assertEquals(200, cars.get(1).getHp()); // Самое маленькое четное
            assertEquals(250, cars.get(2).getHp());
            assertEquals(300, cars.get(3).getHp());
        }

        @Test
        @DisplayName("sort() с нечетными значениями должен оставлять их на месте")
        void sort_WithOddValues_ShouldKeepThemInPlace() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1); // 301 (нечет)
            cars.add(CAR1); // 301 (нечет)

            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals(301, cars.get(0).getHp());
            assertEquals(301, cars.get(1).getHp());
        }

        @Test
        @DisplayName("sort() с только четными значениями должен сортировать все")
        void sort_WithOnlyEvenValues_ShouldSortAll() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR3); // 300
            cars.add(CAR4); // 200
            cars.add(CAR2); // 250

            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals(200, cars.get(0).getHp());
            assertEquals(250, cars.get(1).getHp());
            assertEquals(300, cars.get(2).getHp());
        }
    }

    @Nested
    @DisplayName("Тесты сортировки по году выпуска")
    class SortByYearTests {

        @Test
        @DisplayName("sort() должен сортировать только четные года")
        void sort_ShouldSortOnlyEvenYears() {
            // Given
            Function<Car, Integer> extractor = Car::getYearOfProduction;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1); // 2022 (чет)
            cars.add(CAR2); // 2023 (нечет)
            cars.add(CAR3); // 2024 (чет)
            cars.add(CAR4); // 2021 (нечет)

            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            sorter.sort(cars, comparator);

            // Then
            // Четные года должны быть отсортированы: 2022, 2024
            assertEquals(2022, cars.get(0).getYearOfProduction());
            assertEquals(2023, cars.get(1).getYearOfProduction()); // нечет
            assertEquals(2024, cars.get(2).getYearOfProduction());
            assertEquals(2021, cars.get(3).getYearOfProduction()); // нечет
        }
    }

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

        @Test
        @DisplayName("sort() с одним четным элементом")
        void sort_WithOneEvenElement() {
            // Given
            Function<Car, Integer> extractor = Car::getHp;
            EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR2); // 250 (чет)
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            sorter.sort(cars, comparator);

            // Then
            assertEquals(250, cars.get(0).getHp());
        }

        @Nested
        @DisplayName("Тесты с разными экстракторами")
        class DifferentExtractorsTests {

            @Test
            @DisplayName("sort() с экстрактором по году")
            void sort_WithYearExtractor() {
                // Given
                Function<Car, Integer> extractor = Car::getYearOfProduction;
                EvenOddNumberSort sorter = new EvenOddNumberSort(extractor);
                CustomArrayList<Car> cars = new CustomArrayList<>();
                cars.add(new Car.Builder("A").yearOfProduction(2021).hp(100).build());
                cars.add(new Car.Builder("B").yearOfProduction(2022).hp(100).build());
                cars.add(new Car.Builder("C").yearOfProduction(2023).hp(100).build());
                cars.add(new Car.Builder("D").yearOfProduction(2024).hp(100).build());

                Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

                // When
                sorter.sort(cars, comparator);

                // Then
                // Четные года (2022, 2024) должны быть отсортированы
                assertEquals(2021, cars.get(0).getYearOfProduction());
                assertEquals(2022, cars.get(1).getYearOfProduction());
                assertEquals(2023, cars.get(2).getYearOfProduction());
                assertEquals(2024, cars.get(3).getYearOfProduction());
            }
        }
    }
}