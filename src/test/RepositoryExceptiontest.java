package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import repository.Repository;
import repository.RepositoryException;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты производительности RepositoryException")
class RepositoryExceptionTest {

    @RepeatedTest(100)
    @DisplayName("Создание исключения не должно занимать много времени")
    void exceptionCreationShouldBeFast() {
        assertTimeout(Duration.ofMillis(10), () -> {
            RepositoryException exception = new RepositoryException("Тестовое сообщение");
            assertNotNull(exception);
        });
    }

    @Test
    @DisplayName("Конструктор с сообщением должен сохранять переданное сообщение")
    void constructorWithMessage_ShouldStoreMessage() {
        // Given
        String expectedMessage = "Файл данных не найден";

        // When
        RepositoryException exception = new RepositoryException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Конструктор с пустым сообщением должен создавать исключение с пустым сообщением")
    void constructorWithEmptyMessage_ShouldStoreEmptyMessage() {
        // When
        RepositoryException exception = new RepositoryException("");

        // Then
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("Конструктор с null сообщением должен создавать исключение с null сообщением")
    void constructorWithNullMessage_ShouldStoreNullMessage() {
        // When
        RepositoryException exception = new RepositoryException(null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Конструктор с сообщением должен корректно работать с разными типами сообщений")
    void constructorWithMessage_ShouldWorkWithDifferentMessages() {
        // Given
        String[] testMessages = {
                "Ошибка доступа к файлу",
                "Не удалось сохранить данные",
                "Список автомобилей не найден",
                "12345",
                "Special chars: !@#$%^&*()",
                "Многострочное\nсообщение\nс переносами"
        };

        // When & Then
        for (String message : testMessages) {
            RepositoryException exception = new RepositoryException(message);
            assertEquals(message, exception.getMessage());
        }
    }
}

@Nested
@DisplayName("Тесты наследования")
class InheritanceTests {

    @Test
    @DisplayName("RepositoryException должен быть наследником Exception")
    void shouldExtendException() {
        // When
        RepositoryException exception = new RepositoryException();

        // Then
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("RepositoryException должен иметь serialVersionUID")
    void shouldHaveSerialVersionUID() throws Exception {
        // When
        java.lang.reflect.Field field = RepositoryException.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        long serialVersionUID = field.getLong(null);

        // Then
        assertEquals(5521672180746254687L, serialVersionUID);
    }

    @Test
    @DisplayName("RepositoryException должен быть сериализуемым")
    void shouldBeSerializable() {
        // Given
        RepositoryException original = new RepositoryException("Тестовое сообщение");

        // When & Then
        assertDoesNotThrow(() -> {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(baos.toByteArray());
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
            RepositoryException deserialized = (RepositoryException) ois.readObject();
            ois.close();

            assertEquals(original.getMessage(), deserialized.getMessage());
        });
    }
}