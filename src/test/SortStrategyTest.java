package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.sort.SortStrategy;
import utils.CustomArrayList;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты интерфейса SortStrategy")
class SortStrategyTest {

    // Тестовая реализация интерфейса
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
    }

    private static final Car CAR1 = new Car.Builder("BMW")
            .yearOfProduction(2022)
            .hp(300)
            .build();

    private static final Car CAR2 = new Car.Builder("Audi")
            .yearOfProduction(2023)
            .hp(250)
            .build();

    @Nested
    @DisplayName("Тесты вызова метода sort")
    class SortMethodTests {

        @Test
        @DisplayName("sort() должен вызываться с корректными параметрами")
        void sort_ShouldBeCalledWithCorrectParameters() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertSame(cars, strategy.getLastList());
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен принимать пустой список")
        void sort_WithEmptyList_ShouldAcceptEmptyList() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> emptyList = new CustomArrayList<>();
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(emptyList, comparator);

            // Then
            assertTrue(strategy.wasCalled());
            assertEquals(0, strategy.getLastList().size());
        }

        @Test
        @DisplayName("sort() должен принимать null компаратор")
        void sort_WithNullComparator_ShouldAcceptNull() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

            // When
            strategy.sort(cars, null);

            // Then
            assertTrue(strategy.wasCalled());
            assertNull(strategy.getLastComparator());
        }
    }

    @Nested
    @DisplayName("Тесты с разными компараторами")
    class DifferentComparatorsTests {

        @Test
        @DisplayName("sort() должен работать с компаратором по hp")
        void sort_WithHpComparator_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с компаратором по году")
        void sort_WithYearComparator_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getYearOfProduction);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с компаратором по модели")
        void sort_WithModelComparator_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertSame(comparator, strategy.getLastComparator());
        }

        @Test
        @DisplayName("sort() должен работать с обратным компаратором")
        void sort_WithReversedComparator_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp).reversed();

            // When
            strategy.sort(cars, comparator);

            // Then
            assertSame(comparator, strategy.getLastComparator());
        }
    }

    @Nested
    @DisplayName("Тесты с разными списками")
    class DifferentListsTests {

        @Test
        @DisplayName("sort() должен принимать список с одним элементом")
        void sort_WithSingleElement_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertEquals(1, strategy.getLastList().size());
            assertSame(CAR1, strategy.getLastList().get(0));
        }

        @Test
        @DisplayName("sort() должен принимать список с несколькими элементами")
        void sort_WithMultipleElements_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertEquals(2, strategy.getLastList().size());
            assertSame(CAR1, strategy.getLastList().get(0));
            assertSame(CAR2, strategy.getLastList().get(1));
        }

        @Test
        @DisplayName("sort() должен принимать список с null элементами")
        void sort_WithNullElements_ShouldAccept() {
            // Given
            TestSortStrategy strategy = new TestSortStrategy();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(null);
            cars.add(CAR1);
            cars.add(null);
            Comparator<Car> comparator = Comparator.comparing(Car::getHp);

            // When
            strategy.sort(cars, comparator);

            // Then
            assertEquals(3, strategy.getLastList().size());
            assertNull(strategy.getLastList().get(0));
            assertSame(CAR1, strategy.getLastList().get(1));
            assertNull(strategy.getLastList().get(2));
        }
    }
}
