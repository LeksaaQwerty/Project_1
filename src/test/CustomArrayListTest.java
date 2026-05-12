package test;

import org.junit.jupiter.api.*;
import utils.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты CustomArrayList")
class CustomArrayListTest {

    private CustomArrayList<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomArrayList<>();
    }

    @Nested
    @DisplayName("Тесты add(T t)")
    class AddTests {

        @Test
        @DisplayName("add() должен добавлять элемент в конец списка")
        void add_ShouldAddElementToEnd() {
            assertTrue(list.add("A"));
            assertTrue(list.add("B"));
            assertTrue(list.add("C"));

            assertEquals(3, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
        }

        @Test
        @DisplayName("add() должен увеличивать массив при необходимости")
        void add_ShouldIncreaseArrayWhenFull() {
            for (int i = 0; i < 10; i++) {
                list.add("Element" + i);
            }

            assertEquals(10, list.size());
            assertEquals("Element0", list.get(0));
            assertEquals("Element9", list.get(9));
        }

        @Test
        @DisplayName("add() должен добавлять null элементы")
        void add_ShouldAddNullElements() {
            list.add(null);
            list.add("A");
            list.add(null);

            assertEquals(3, list.size());
            assertNull(list.get(0));
            assertEquals("A", list.get(1));
            assertNull(list.get(2));
        }
    }

    @Nested
    @DisplayName("Тесты add(int index, T t)")
    class AddAtIndexTests {

        @Test
        @DisplayName("add(index) должен добавлять элемент в начало")
        void addAtIndex_ShouldAddToBeginning() {
            list.add("B");
            list.add("C");
            assertTrue(list.add(0, "A"));

            assertEquals(3, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
        }

        @Test
        @DisplayName("add(index) должен добавлять элемент в середину")
        void addAtIndex_ShouldAddToMiddle() {
            list.add("A");
            list.add("C");
            assertTrue(list.add(1, "B"));

            assertEquals(3, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
        }

        @Test
        @DisplayName("add(index) должен добавлять элемент в конец")
        void addAtIndex_ShouldAddToEnd() {
            list.add("A");
            list.add("B");
            assertTrue(list.add(2, "C"));

            assertEquals(3, list.size());
            assertEquals("C", list.get(2));
        }

        @Test
        @DisplayName("add(index) с неверным индексом должен возвращать false")
        void addAtIndex_WithInvalidIndex_ShouldReturnFalse() {
            assertFalse(list.add(-1, "A"));
            assertFalse(list.add(5, "A"));
            assertEquals(0, list.size());
        }
    }

    @Nested
    @DisplayName("Тесты set(int index, T t)")
    class SetTests {

        @Test
        @DisplayName("set() должен заменять элемент по индексу")
        void set_ShouldReplaceElement() {
            list.add("A");
            list.add("B");
            assertTrue(list.set(1, "C"));

            assertEquals("C", list.get(1));
            assertEquals(2, list.size());
        }

        @Test
        @DisplayName("set() с неверным индексом должен возвращать false")
        void set_WithInvalidIndex_ShouldReturnFalse() {
            assertFalse(list.set(-1, "A"));
            assertFalse(list.set(0, "A"));
            assertFalse(list.set(5, "A"));
        }

        @Test
        @DisplayName("set() должен устанавливать null")
        void set_ShouldSetNull() {
            list.add("A");
            assertTrue(list.set(0, null));
            assertNull(list.get(0));
        }
    }

    @Nested
    @DisplayName("Тесты delete(int index)")
    class DeleteTests {

        @Test
        @DisplayName("delete() должен удалять элемент по индексу")
        void delete_ShouldRemoveElement() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertTrue(list.delete(1));

            assertEquals(2, list.size());
            assertEquals("A", list.get(0));
            assertEquals("C", list.get(1));
        }

        @Test
        @DisplayName("delete() с неверным индексом должен возвращать false")
        void delete_WithInvalidIndex_ShouldReturnFalse() {
            assertFalse(list.delete(-1));
            assertFalse(list.delete(0));
            assertFalse(list.delete(5));
        }

        @Test
        @DisplayName("delete() должен удалять первый элемент")
        void delete_ShouldDeleteFirst() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertTrue(list.delete(0));

            assertEquals(2, list.size());
            assertEquals("B", list.get(0));
        }

        @Test
        @DisplayName("delete() должен удалять последний элемент")
        void delete_ShouldDeleteLast() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertTrue(list.delete(2));

            assertEquals(2, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
        }
    }

    @Nested
    @DisplayName("Тесты deleteFirst(T t)")
    class DeleteFirstTests {

        @Test
        @DisplayName("deleteFirst() должен удалять первое вхождение")
        void deleteFirst_ShouldRemoveFirstOccurrence() {
            list.add("A");
            list.add("B");
            list.add("A");

            assertTrue(list.deleteFirst("A"));

            assertEquals(2, list.size());
            assertEquals("B", list.get(0));
            assertEquals("A", list.get(1));
        }

        @Test
        @DisplayName("deleteFirst() с null должен удалять первый null")
        void deleteFirst_ShouldRemoveFirstNull() {
            list.add("A");
            list.add(null);
            list.add("B");
            list.add(null);

            assertTrue(list.deleteFirst(null));

            assertEquals(3, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertNull(list.get(2));
        }

        @Test
        @DisplayName("deleteFirst() с несуществующим элементом должен возвращать false")
        void deleteFirst_WithNonExistentElement_ShouldReturnFalse() {
            list.add("A");
            list.add("B");

            assertFalse(list.deleteFirst("C"));
            assertEquals(2, list.size());
        }
    }

    @Nested
    @DisplayName("Тесты deleteAll(T t)")
    class DeleteAllTests {

        @Test
        @DisplayName("deleteAll() должен удалять все вхождения")
        void deleteAll_ShouldRemoveAllOccurrences() {
            list.add("A");
            list.add("B");
            list.add("A");
            list.add("C");
            list.add("A");

            assertTrue(list.deleteAll("A"));

            assertEquals(2, list.size());
            assertEquals("B", list.get(0));
            assertEquals("C", list.get(1));
        }

        @Test
        @DisplayName("deleteAll() с null должен удалять все null")
        void deleteAll_ShouldRemoveAllNulls() {
            list.add("A");
            list.add(null);
            list.add("B");
            list.add(null);
            list.add("C");

            assertTrue(list.deleteAll(null));

            assertEquals(3, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
            assertEquals("C", list.get(2));
        }

        @Test
        @DisplayName("deleteAll() с несуществующим элементом должен возвращать false")
        void deleteAll_WithNonExistentElement_ShouldReturnFalse() {
            list.add("A");
            list.add("B");

            assertFalse(list.deleteAll("C"));
            assertEquals(2, list.size());
        }
    }

    @Nested
    @DisplayName("Тесты get(int index)")
    class GetTests {

        @Test
        @DisplayName("get() должен возвращать элемент по индексу")
        void get_ShouldReturnElement() {
            list.add("A");
            list.add("B");

            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
        }

        @Test
        @DisplayName("get() с неверным индексом должен выбрасывать исключение")
        void get_WithInvalidIndex_ShouldThrowException() {
            list.add("A");

            assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        }
    }

    @Nested
    @DisplayName("Тесты contains(T t)")
    class ContainsTests {

        @Test
        @DisplayName("contains() должен находить существующий элемент")
        void contains_WithExistingElement_ShouldReturnTrue() {
            list.add("A");
            list.add("B");

            assertTrue(list.contains("A"));
            assertTrue(list.contains("B"));
        }

        @Test
        @DisplayName("contains() с null должен находить null")
        void contains_WithNull_ShouldFindNull() {
            list.add("A");
            list.add(null);
            list.add("B");

            assertTrue(list.contains(null));
        }

        @Test
        @DisplayName("contains() с несуществующим элементом должен возвращать false")
        void contains_WithNonExistentElement_ShouldReturnFalse() {
            list.add("A");
            list.add("B");

            assertFalse(list.contains("C"));
        }
    }

    @Nested
    @DisplayName("Тесты indexOf(T t)")
    class IndexOfTests {

        @Test
        @DisplayName("indexOf() должен возвращать индекс первого вхождения")
        void indexOf_ShouldReturnFirstIndex() {
            list.add("A");
            list.add("B");
            list.add("A");

            assertEquals(0, list.indexOf("A"));
            assertEquals(1, list.indexOf("B"));
        }

        @Test
        @DisplayName("indexOf() с null должен находить null")
        void indexOf_ShouldFindNull() {
            list.add("A");
            list.add(null);
            list.add("B");

            assertEquals(1, list.indexOf(null));
        }

        @Test
        @DisplayName("indexOf() с несуществующим элементом должен возвращать -1")
        void indexOf_WithNonExistentElement_ShouldReturnMinusOne() {
            list.add("A");
            list.add("B");

            assertEquals(-1, list.indexOf("C"));
        }
    }

    @Nested
    @DisplayName("Тесты clear()")
    class ClearTests {

        @Test
        @DisplayName("clear() должен очищать список")
        void clear_ShouldEmptyList() {
            list.add("A");
            list.add("B");
            list.add("C");

            list.clear();

            assertEquals(0, list.size());
            assertTrue(list.isEmpty());
        }

        @Test
        @DisplayName("clear() должен возвращать себя")
        void clear_ShouldReturnItself() {
            list.add("A");

            CustomArrayList<String> result = list.clear();

            assertSame(list, result);
        }
    }

    @Nested
    @DisplayName("Тесты size() и isEmpty()")
    class SizeAndEmptyTests {

        @Test
        @DisplayName("size() должен возвращать количество элементов")
        void size_ShouldReturnElementCount() {
            assertEquals(0, list.size());

            list.add("A");
            assertEquals(1, list.size());

            list.add("B");
            assertEquals(2, list.size());
        }

        @Test
        @DisplayName("isEmpty() должен проверять пустоту списка")
        void isEmpty_ShouldCheckIfListIsEmpty() {
            assertTrue(list.isEmpty());

            list.add("A");
            assertFalse(list.isEmpty());

            list.clear();
            assertTrue(list.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты toString()")
    class ToStringTests {

        @Test
        @DisplayName("toString() должен возвращать строковое представление")
        void toString_ShouldReturnStringRepresentation() {
            list.add("A");
            list.add("B");
            list.add("C");

            assertEquals("[A, B, C]", list.toString());
        }

        @Test
        @DisplayName("toString() для пустого списка")
        void toString_ForEmptyList_ShouldReturnEmptyBrackets() {
            assertEquals("[]", list.toString());
        }

        @Test
        @DisplayName("toString() с null элементами")
        void toString_WithNullElements() {
            list.add("A");
            list.add(null);
            list.add("C");

            assertEquals("[A, null, C]", list.toString());
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Сложный сценарий работы со списком")
        void complexScenario_ShouldWorkCorrectly() {
            // Добавление элементов
            list.add("A");
            list.add("B");
            list.add("C");
            assertEquals(3, list.size());

            // Вставка в середину
            list.add(1, "X");
            assertEquals("[A, X, B, C]", list.toString());

            // Замена элемента
            list.set(2, "Y");
            assertEquals("[A, X, Y, C]", list.toString());

            // Удаление элемента
            list.delete(1);
            assertEquals("[A, Y, C]", list.toString());

            // Проверка наличия
            assertTrue(list.contains("Y"));
            assertEquals(1, list.indexOf("Y"));

            // Очистка
            list.clear();
            assertTrue(list.isEmpty());
            assertEquals("[]", list.toString());
        }

        @Test
        @DisplayName("Работа с null элементами")
        void nullElements_ShouldWorkCorrectly() {
            list.add(null);
            list.add("A");
            list.add(null);
            list.add("B");

            assertEquals(4, list.size());
            assertTrue(list.contains(null));
            assertEquals(0, list.indexOf(null));

            list.deleteFirst(null);
            assertEquals(3, list.size());
            assertEquals("A", list.get(0));

            list.deleteAll(null);
            assertEquals(2, list.size());
            assertEquals("A", list.get(0));
            assertEquals("B", list.get(1));
        }
    }
}
