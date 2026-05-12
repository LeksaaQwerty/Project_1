package test;

import org.junit.jupiter.api.*;
import service.ServiceException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты ServiceException")
class ServiceExceptionTest {

    @Nested
    @DisplayName("Тесты конструктора по умолчанию")
    class DefaultConstructorTests {

        @Test
        @DisplayName("Конструктор по умолчанию должен создавать исключение")
        void defaultConstructor_ShouldCreateException() {
            ServiceException exception = new ServiceException();

            assertNotNull(exception);
            assertNull(exception.getMessage());
        }

        @Test
        @DisplayName("Конструктор по умолчанию должен создавать исключение без причины")
        void defaultConstructor_ShouldHaveNoCause() {
            ServiceException exception = new ServiceException();

            assertNull(exception.getCause());
        }
    }

    @Nested
    @DisplayName("Тесты конструктора с сообщением")
    class MessageConstructorTests {

        @Test
        @DisplayName("Конструктор с сообщением должен сохранять сообщение")
        void constructorWithMessage_ShouldStoreMessage() {
            String message = "Ошибка сервиса";
            ServiceException exception = new ServiceException(message);

            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("Конструктор с пустым сообщением")
        void constructorWithEmptyMessage_ShouldStoreEmptyMessage() {
            ServiceException exception = new ServiceException("");

            assertEquals("", exception.getMessage());
        }

        @Test
        @DisplayName("Конструктор с null сообщением")
        void constructorWithNullMessage_ShouldStoreNull() {
            ServiceException exception = new ServiceException(null);

            assertNull(exception.getMessage());
        }

        @Test
        @DisplayName("Конструктор с разными сообщениями")
        void constructor_ShouldWorkWithDifferentMessages() {
            String[] messages = {
                    "Не удалось сохранить",
                    "Список не найден",
                    "Ошибка валидации",
                    "12345",
                    "!@#$%^&*()"
            };

            for (String message : messages) {
                ServiceException exception = new ServiceException(message);
                assertEquals(message, exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Тесты наследования")
    class InheritanceTests {

        @Test
        @DisplayName("ServiceException должен быть наследником Exception")
        void shouldExtendException() {
            ServiceException exception = new ServiceException();

            assertTrue(exception instanceof Exception);
            assertTrue(exception instanceof Throwable);
        }

        @Test
        @DisplayName("ServiceException должно корректно выбрасываться")
        void shouldBeThrown() {
            assertThrows(ServiceException.class, () -> {
                throw new ServiceException("Тестовая ошибка");
            });
        }

        @Test
        @DisplayName("ServiceException должно перехватываться в catch блоке")
        void shouldBeCaught() throws ServiceException {
            boolean caught = false;

            try {
                throw new ServiceException("Ошибка");
            } catch (ServiceException e) {
                caught = true;
                assertEquals("Ошибка", e.getMessage());
            }

            assertTrue(caught);
        }
    }

    @Nested
    @DisplayName("Тесты сериализации")
    class SerializationTests {

        @Test
        @DisplayName("ServiceException должен иметь serialVersionUID")
        void shouldHaveSerialVersionUID() throws Exception {
            var field = ServiceException.class.getDeclaredField("serialVersionUID");
            field.setAccessible(true);
            long serialVersionUID = field.getLong(null);

            assertEquals(1L, serialVersionUID);
        }
    }
}