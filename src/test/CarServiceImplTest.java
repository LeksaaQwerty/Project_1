package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.CarService;
import service.CarServiceImpl;
import service.ServiceException;
import utils.CustomArrayList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceImplTest {

    private CarService carService;
    private static final Car TEST_CAR = new Car.Builder("Test Car")
            .yearOfProduction(2022)
            .hp(150)
            .build();

    @BeforeEach
    void setUp() throws Exception {
        carService = new CarServiceImpl();
        // Очищаем тестовую директорию
        Path testPath = Paths.get("files");
        if (Files.exists(testPath)) {
            Files.walk(testPath)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); }
                        catch (Exception e) {}
                    });
        }
    }

    @Test
    void save_ShouldReturnTrue() throws ServiceException {
        boolean result = carService.save(TEST_CAR, "test");
        assertTrue(result);
    }

    @Test
    void saveAll_ShouldReturnTrue() throws ServiceException {
        CustomArrayList<Car> cars = new CustomArrayList<>();
        cars.add(TEST_CAR);

        boolean result = carService.saveAll(cars, "testList");
        assertTrue(result);
    }

    @Test
    void getListNames_WhenNoLists_ShouldThrowException() {
        assertThrows(ServiceException.class, () -> carService.getListNames());
    }

    @Test
    void saveCarListNames_ShouldReturnTrue() throws ServiceException {
        CustomArrayList<String> names = new CustomArrayList<>();
        names.add("testList");

        boolean result = carService.saveCarListNames(names);
        assertTrue(result);
    }
}