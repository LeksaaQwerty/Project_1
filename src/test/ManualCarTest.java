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
    void setUp() {
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    @Test
    void build_WithValidInput_ShouldCreateCar() {
        // Given
        String input = "Toyota Camry\n150\n2022\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        // When
        Car car = ManualCar.build();

        // Then
        assertNotNull(car);
        assertEquals("Toyota Camry", car.getModel());
        assertEquals(150, car.getHp());
        assertEquals(2022, car.getYearOfProduction());
    }
}