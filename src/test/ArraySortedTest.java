package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.sort.ArraySorted;
import service.sort.SortStrategy;
import utils.CustomArrayList;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты ArraySorted")
class ArraySortedTest {

    private static class TestSortStrategy implements SortStrategy {
        private boolean wasCalled = false;
        private CustomArrayList<Car> lastList = null;
        private Comparator<Car> lastComparator = null;

        @Override
        public void sort(CustomArrayList<Car> car, Comparator<Car> comparator) {
            wasCalled = true;
            lastList = car;
            lastComparator = comparator;
        }

        public boolean wasCalled() { return wasCalled; }
        public CustomArrayList<Car> getLastList() { return lastList; }
        public Comparator<Car> getLastComparator() { return lastComparator; }
        public void reset() {
            wasCalled = false;
            lastList = null;
            lastComparator = null;
        }
    }

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
    @DisplayName("Тесты конструктора и метода sort")
    class SortTests {

        @Test
        @DisplayName("sort() должен вызывать стратегию сортировки")
        void sort_ShouldCallSortStrategy() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(cars, strategy.getLastList());
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен передавать правильные параметры в стратегию")
        void sort_ShouldPassCorrectParameters() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            cars.add(CAR3);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertEquals(3, strategy.getLastList().size());
            assertEquals(CAR1, strategy.getLastList().get(0));
            assertEquals(CAR2, strategy.getLastList().get(1));
            assertEquals(CAR3, strategy.getLastList().get(2));
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() с пустым списком")
        void sort_WithEmptyList_ShouldCallStrategy() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> emptyList = new CustomArrayList<>();
            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            arraySorted.sort(emptyList, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertEquals(0, strategy.getLastList().size());
        }
    }

    @Nested
    @DisplayName("Тесты с разными компараторами")
    class DifferentComparatorsTests {

        @Test
        @DisplayName("sort() должен работать с компаратором по модели")
        void sort_WithModelComparator() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с компаратором по мощности")
        void sort_WithHpComparator() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с компаратором по году")
        void sort_WithYearComparator() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с обратным компаратором")
        void sort_WithReversedComparator() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getModel).reversed();

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(comparator, strategy.getLastComparator());
        }
    }

    @Nested
    @DisplayName("Тесты с разными размерами списков")
    class DifferentSizeListsTests {

        @Test
        @DisplayName("sort() с одним элементом")
        void sort_WithSingleElement() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertEquals(1, strategy.getLastList().size());
        }

        @Test
        @DisplayName("sort() с большим списком")
        void sort_WithLargeList() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            ArraySorted arraySorted = new ArraySorted(strategy);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            for (int i = 0; i < 100; i++) {
                cars.add(CAR1);
            }
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            arraySorted.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertEquals(100, strategy.getLastList().size());
        }
    }
}

// Простая реализация SortStrategy для тестов
class SimpleSortStrategy implements SortStrategy {
    @Override
    public void sort(CustomArrayList<Car> car, Comparator<Car> comparator) {
        // Простая пузырьковая сортировка для тестов
        for (int i = 0; i < car.size() - 1; i++) {
            for (int j = 0; j < car.size() - i - 1; j++) {
                if (comparator.compare(car.get(j), car.get(j + 1)) > 0) {
                    Car temp = car.get(j);
                    car.set(j, car.get(j + 1));
                    car.set(j + 1, temp);
                }
            }
        }
    }
}
