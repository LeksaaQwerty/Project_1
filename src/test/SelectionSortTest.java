package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.sort.SelectionSort;
import utils.CustomArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты SelectionSort")
class SelectionSortTest {

    private SelectionSort selectionSort;

    @BeforeEach
    void setUp() {
        selectionSort = new SelectionSort();
    }

    @Nested
    @DisplayName("Тесты сортировки по разным критериям")
    class SortByDifferentCriteriaTests {

        @Test
        @DisplayName("sort() должен сортировать автомобили по мощности (по возрастанию)")
        void sort_ShouldSortByHpAscending() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(new Car.Builder("BMW").yearOfProduction(2022).hp(300).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2023).hp(250).build());
            cars.add(new Car.Builder("Tesla").yearOfProduction(2024).hp(450).build());
            cars.add(new Car.Builder("Mercedes").yearOfProduction(2021).hp(200).build());

            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            selectionSort.sort(cars, comparator);

            // Then
            assertEquals(200, cars.get(0).getHp());
            assertEquals(250, cars.get(1).getHp());
            assertEquals(300, cars.get(2).getHp());
            assertEquals(450, cars.get(3).getHp());
        }

        @Test
        @DisplayName("sort() должен сортировать автомобили по году выпуска")
        void sort_ShouldSortByYearAscending() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(new Car.Builder("BMW").yearOfProduction(2023).hp(300).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2021).hp(250).build());
            cars.add(new Car.Builder("Tesla").yearOfProduction(2024).hp(450).build());
            cars.add(new Car.Builder("Mercedes").yearOfProduction(2022).hp(200).build());

            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            selectionSort.sort(cars, comparator);

            // Then
            assertEquals(2021, cars.get(0).getYearOfProduction());
            assertEquals(2022, cars.get(1).getYearOfProduction());
            assertEquals(2023, cars.get(2).getYearOfProduction());
            assertEquals(2024, cars.get(3).getYearOfProduction());
        }

        @Test
        @DisplayName("sort() должен сортировать автомобили по модели (алфавитно)")
        void sort_ShouldSortByModelAlphabetically() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(new Car.Builder("BMW").yearOfProduction(2022).hp(300).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2023).hp(250).build());
            cars.add(new Car.Builder("Tesla").yearOfProduction(2024).hp(450).build());
            cars.add(new Car.Builder("Mercedes").yearOfProduction(2021).hp(200).build());

            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            selectionSort.sort(cars, comparator);

            // Then
            assertEquals("Audi", cars.get(0).getModel());
            assertEquals("BMW", cars.get(1).getModel());
            assertEquals("Mercedes", cars.get(2).getModel());
            assertEquals("Tesla", cars.get(3).getModel());
        }
    }

    @Nested
    @DisplayName("Граничные тесты")
    class BoundaryTests {

        @Test
        @DisplayName("sort() с пустым списком не должен падать")
        void sort_WithEmptyList_ShouldDoNothing() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When & Then
            assertDoesNotThrow(() -> selectionSort.sort(cars, comparator));
            assertEquals(0, cars.size());
        }

        @Test
        @DisplayName("sort() с одинаковыми годами")
        void sort_WithSameYear() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(new Car.Builder("BMW").yearOfProduction(2022).hp(300).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2022).hp(250).build());
            cars.add(new Car.Builder("Tesla").yearOfProduction(2022).hp(450).build());

            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            selectionSort.sort(cars, comparator);

            // Then
            assertEquals(2022, cars.get(0).getYearOfProduction());
            assertEquals(2022, cars.get(1).getYearOfProduction());
            assertEquals(2022, cars.get(2).getYearOfProduction());
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Сортировка с цепочкой компараторов")
        void sort_WithChainedComparator() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(new Car.Builder("BMW").yearOfProduction(2022).hp(300).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2022).hp(250).build());
            cars.add(new Car.Builder("Audi").yearOfProduction(2023).hp(200).build());
            cars.add(new Car.Builder("BMW").yearOfProduction(2023).hp(350).build());

            Comparator<Car> comparator = Comparator.comparing(Car::getModel)
                    .thenComparing(Car::getYearOfProduction);

            // When
            selectionSort.sort(cars, comparator);

            // Then
            assertEquals("Audi", cars.get(0).getModel());
            assertEquals(2022, cars.get(0).getYearOfProduction());
            assertEquals("Audi", cars.get(1).getModel());
            assertEquals(2023, cars.get(1).getYearOfProduction());
            assertEquals("BMW", cars.get(2).getModel());
            assertEquals(2022, cars.get(2).getYearOfProduction());
            assertEquals("BMW", cars.get(3).getModel());
            assertEquals(2023, cars.get(3).getYearOfProduction());
        }
    }
}
