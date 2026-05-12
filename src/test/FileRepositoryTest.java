package test;

import model.Car;
import org.junit.jupiter.api.*;
import repository.FileRepository;
import repository.RepositoryException;
import utils.CustomArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;



class FileRepositoryTest {

    private static final String ORIGINAL_DIR = "files";
    private static final String TEST_DIR = "files_test_temp";

    @BeforeEach
    void setUp() throws IOException {
        // Переименовываем оригинальную папку если она существует
        Path original = Paths.get(ORIGINAL_DIR);
        Path test = Paths.get(TEST_DIR);

        if (Files.exists(original)) {
            Files.move(original, test);
        }

        // Создаем новую папку files для тестов
        Files.createDirectories(original);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем тестовую папку files
        Path original = Paths.get(ORIGINAL_DIR);
        if (Files.exists(original)) {
            Files.walk(original)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); }
                        catch (IOException e) {}
                    });
        }

        // Восстанавливаем оригинальную папку
        Path test = Paths.get(TEST_DIR);
        if (Files.exists(test)) {
            Files.move(test, original);
        }
    }

    @Test
    void testSaveCar() throws RepositoryException {
        FileRepository repository = new FileRepository();
        Car car = new Car.Builder("Test Car").yearOfProduction(2022).hp(150).build();

        boolean result = repository.save(car, "test");

        assertTrue(result);
    }

    @Test
    void testSaveAndReadCar() throws RepositoryException {
        FileRepository repository = new FileRepository();
        Car car = new Car.Builder("Toyota").yearOfProduction(2022).hp(150).build();

        repository.save(car, "myCar");
        CustomArrayList<Car> cars = repository.readAll(10, "myCar");

        assertEquals(1, cars.size());
        assertEquals("Toyota", cars.get(0).getModel());
    }

    @Test
    void testSaveList() throws RepositoryException {
        FileRepository repository = new FileRepository();
        CustomArrayList<Car> cars = new CustomArrayList<>();
        cars.add(new Car.Builder("BMW").yearOfProduction(2023).hp(300).build());
        cars.add(new Car.Builder("Audi").yearOfProduction(2022).hp(200).build());

        boolean result = repository.save(cars, "list");

        assertTrue(result);
    }
}
