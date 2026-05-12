package test;

import model.Car;
import model.ManualCar;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ManualCarTest {

    private InputStream originalIn;

    @BeforeEach
    void saveOriginalIn() {
        originalIn = System.in;
    }

    @AfterEach
    void restoreOriginalIn() {
        System.setIn(originalIn);
    }

    @Test
    void testBuild() {
        System.setIn(new ByteArrayInputStream("Test\n100\n2022\n".getBytes(StandardCharsets.UTF_8)));

        Car car = ManualCar.build();

        assertNotNull(car);
        assertEquals("Test", car.getModel());
    }
}
