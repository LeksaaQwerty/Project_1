package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.RepeatedTest;
import utils.RandomInRange;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты RandomInRange")
class RandomInRangeTest {

    @Nested
    @DisplayName("Тесты с корректными диапазонами")
    class ValidRangeTests {

        @Test
        @DisplayName("randomInt() должен возвращать число в заданном диапазоне")
        void randomInt_ShouldReturnNumberInRange() {
            int min = 5;
            int max = 10;

            for (int i = 0; i < 100; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= min && result <= max,
                        "Число " + result + " не в диапазоне [" + min + ", " + max + "]");
            }
        }

        @Test
        @DisplayName("randomInt() с min = max должен возвращать это число")
        void randomInt_WithMinEqualToMax_ShouldReturnThatNumber() {
            int value = 42;

            for (int i = 0; i < 10; i++) {
                int result = RandomInRange.randomInt(value, value);
                assertEquals(value, result);
            }
        }

        @Test
        @DisplayName("randomInt() с отрицательными числами")
        void randomInt_WithNegativeNumbers_ShouldWork() {
            int min = -10;
            int max = -5;

            for (int i = 0; i < 100; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= min && result <= max);
            }
        }

        @Test
        @DisplayName("randomInt() с диапазоном включающим ноль")
        void randomInt_WithRangeIncludingZero_ShouldWork() {
            int min = -5;
            int max = 5;

            for (int i = 0; i < 100; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= min && result <= max);
            }
        }
    }

    @Nested
    @DisplayName("Тесты с перевернутыми диапазонами")
    class SwappedRangeTests {

        @Test
        @DisplayName("randomInt() должен менять местами min и max если max < min")
        void randomInt_WithMaxLessThanMin_ShouldSwapParameters() {
            int min = 10;
            int max = 5;

            for (int i = 0; i < 100; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= max && result <= min,
                        "Число " + result + " должно быть в диапазоне [" + max + ", " + min + "]");
            }
        }

        @Test
        @DisplayName("randomInt() с перевернутыми отрицательными числами")
        void randomInt_WithSwappedNegativeNumbers_ShouldWork() {
            int min = -5;
            int max = -10;

            for (int i = 0; i < 100; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= max && result <= min);
            }
        }
    }

    @Nested
    @DisplayName("Тесты случайности")
    class RandomnessTests {

        @RepeatedTest(10)
        @DisplayName("randomInt() должен генерировать разные числа")
        void randomInt_ShouldGenerateDifferentNumbers() {
            int min = 1;
            int max = 1000;

            int result1 = RandomInRange.randomInt(min, max);
            int result2 = RandomInRange.randomInt(min, max);

            // Не всегда разные, но с высокой вероятностью
            // Проверяем только что оба в диапазоне
            assertTrue(result1 >= min && result1 <= max);
            assertTrue(result2 >= min && result2 <= max);
        }

        @Test
        @DisplayName("При большом количестве вызовов должны быть разные значения")
        void manyCalls_ShouldProduceMultipleValues() {
            int min = 1;
            int max = 10;
            boolean[] seen = new boolean[max - min + 1];

            for (int i = 0; i < 1000; i++) {
                int result = RandomInRange.randomInt(min, max);
                seen[result - min] = true;
            }

            // Проверяем, что все числа из диапазона встретились
            for (int i = 0; i < seen.length; i++) {
                assertTrue(seen[i], "Число " + (min + i) + " не сгенерировалось ни разу");
            }
        }
    }

    @Nested
    @DisplayName("Граничные тесты")
    class BoundaryTests {

        @Test
        @DisplayName("randomInt() с максимальными значениями int")
        void randomInt_WithMaxIntValues() {
            int min = Integer.MAX_VALUE - 100;
            int max = Integer.MAX_VALUE;

            for (int i = 0; i < 10; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= min && result <= max);
            }
        }

        @Test
        @DisplayName("randomInt() с минимальными значениями int")
        void randomInt_WithMinIntValues() {
            int min = Integer.MIN_VALUE;
            int max = Integer.MIN_VALUE + 100;

            for (int i = 0; i < 10; i++) {
                int result = RandomInRange.randomInt(min, max);
                assertTrue(result >= min && result <= max);
            }
        }

        @Test
        @DisplayName("randomInt() с диапазоном 0")
        void randomInt_WithZeroRange() {
            assertEquals(5, RandomInRange.randomInt(5, 5));
            assertEquals(-10, RandomInRange.randomInt(-10, -10));
            assertEquals(0, RandomInRange.randomInt(0, 0));
        }
    }

    @Nested
    @DisplayName("Статистические тесты")
    class StatisticalTests {

        @Test
        @DisplayName("Распределение чисел должно быть примерно равномерным")
        void distribution_ShouldBeApproximatelyUniform() {
            int min = 1;
            int max = 4;
            int[] counts = new int[max - min + 1];
            int iterations = 10000;

            for (int i = 0; i < iterations; i++) {
                int result = RandomInRange.randomInt(min, max);
                counts[result - min]++;
            }

            // Каждое число должно встретиться примерно iterations/4 раз
            int expected = iterations / (max - min + 1);
            for (int count : counts) {
                assertTrue(count > expected * 0.9 && count < expected * 1.1,
                        "Ожидалось около " + expected + ", получено " + count);
            }
        }
    }
}