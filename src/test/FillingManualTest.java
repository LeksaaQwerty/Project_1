package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.ServiceException;
import service.filling.FillingManual;
import utils.CustomArrayList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class FillingManualTest {

    private FillingManual fillingManual;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        fillingManual = new FillingManual();
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void fillOut_WithZeroSize_ShouldThrowException() {
        assertThrows(ServiceException.class,
                () -> fillingManual.fillOut(0, "test"));
    }

    @Test
    void fillOut_WithNegativeSize_ShouldThrowException() {
        assertThrows(ServiceException.class,
                () -> fillingManual.fillOut(-1, "test"));
    }

    @Test
    void fillOut_WithInvalidInput_ShouldRetry() throws ServiceException {
        // Given (неверная мощность, затем правильная)
        String input = "Toyota Camry\n0\n150\n2022\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        // When
        CustomArrayList<Car> result = fillingManual.fillOut(1, "test");

        // Then
        assertNotNull(result);
        assertEquals(150, result.get(0).getHp());
    }
}