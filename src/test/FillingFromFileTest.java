package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.ServiceException;
import service.filling.FillingFromFile;
import utils.CustomArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FillingFromFileSimpleTest {

    private FillingFromFile fillingFromFile;

    @BeforeEach
    void setUp() throws IOException {
        fillingFromFile = new FillingFromFile();

        // Создаем тестовый файл
        Path path = Paths.get("files", "test.txt");
        Files.createDirectories(path.getParent());
        Files.writeString(path, "Toyota Camry//2022//150///");
    }

    @AfterEach
    void tearDown() throws IOException {
        Path path = Paths.get("files", "test.txt");
        Files.deleteIfExists(path);
    }

    @Test
    void fillOut_WithValidFile_ShouldReturnCarList() throws ServiceException {
        CustomArrayList<Car> result = fillingFromFile.fillOut(10, "test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota Camry", result.get(0).getModel());
    }

    @Test
    void fillOut_WithNonExistentFile_ShouldThrowServiceException() {
        assertThrows(ServiceException.class,
                () -> fillingFromFile.fillOut(10, "nonExistent"));
    }
}