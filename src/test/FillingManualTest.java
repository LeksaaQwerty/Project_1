package test;

import org.junit.jupiter.api.*;
import service.ServiceException;
import service.filling.FillingManual;

import java.io.InputStream;
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
}
