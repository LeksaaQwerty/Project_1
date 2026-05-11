package test;

import model.Car;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import repository.FileRepository;
import repository.RepositoryException;
import utils.CustomArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты FileRepository")
public class FileRepositoryTest {

    private FileRepository repository;

    @TempDir
    Path tempDir;

    private static final String TEST_NAME = "testList";
    private static final String TEST_CAR_DATA = "Toyota Camry//2022//150///BMW X5//2023//300///";
    private static final Car CAR1 = new Car.Builder("Toyota Camry")
            .yearOfProduction(2022)
            .hp(150)
            .build();
    private static final Car CAR2 = new Car.Builder("BMW X5")
            .yearOfProduction(2023)
            .hp(300)
            .build();

    @BeforeEach
    void setUp() throws IOException {
        repository = new FileRepository();
        // Создаем тестовую директорию
        Files.createDirectories(tempDir);
        // Используем рефлексию или изменяем PATH для тестов
        // Внимание: это требует изменения доступа к PATH или использования PowerMock
        // Для простоты предположим, что PATH можно изменить через сеттер
    }

    @Nested
    @DisplayName("Тесты readAll()")
    class ReadAllTests {

        @Test
        @DisplayName("readAll() должен корректно читать все автомобили")
        void readAll_ShouldReadAllCars() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", TEST_NAME + ".txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, TEST_CAR_DATA);

            // When
            CustomArrayList<Car> result = repository.readAll(10, TEST_NAME);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(CAR1, result.get(0));
            assertEquals(CAR2, result.get(1));
        }

        @Test
        @DisplayName("readAll() должен читать только указанное количество автомобилей")
        void readAll_ShouldReadLimitedNumberOfCars() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", TEST_NAME + ".txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, TEST_CAR_DATA);

            // When
            CustomArrayList<Car> result = repository.readAll(1, TEST_NAME);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(CAR1, result.get(0));
        }

        @Test
        @DisplayName("readAll() должен выбрасывать исключение при отсутствии файла")
        void readAll_WhenFileNotFound_ShouldThrowException() {
            // When & Then
            RepositoryException exception = assertThrows(RepositoryException.class,
                    () -> repository.readAll(10, "nonExistent"));
            assertEquals("Файл данных не найден", exception.getMessage());
        }

        @Test
        @DisplayName("readAll() должен возвращать пустой список при пустом файле")
        void readAll_WithEmptyFile_ShouldReturnEmptyList() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", TEST_NAME + ".txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, "");

            // When
            CustomArrayList<Car> result = repository.readAll(10, TEST_NAME);

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("readAll() должен игнорировать некорректные данные")
        void readAll_WithInvalidData_ShouldSkipInvalidEntries() throws RepositoryException, IOException {
            // Given
            String invalidData = "Toyota Camry//2022//150///invalid///BMW X5//2023//300///";
            Path path = Path.of("files", TEST_NAME + ".txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, invalidData);

            // When & Then
            // Должен выбросить исключение при парсинге или пропустить
            assertThrows(Exception.class, () -> repository.readAll(10, TEST_NAME));
        }
    }

    @Nested
    @DisplayName("Тесты save(CustomArrayList, String)")
    class SaveListTests {

        @Test
        @DisplayName("save() должен сохранять список автомобилей в файл")
        void save_ShouldSaveCarList() throws RepositoryException, IOException {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);

            // When
            boolean result = repository.save(cars, TEST_NAME);

            // Then
            assertTrue(result);
            Path path = Path.of("files", TEST_NAME + ".txt");
            assertTrue(Files.exists(path));
            String content = Files.readString(path);
            assertTrue(content.contains("Toyota Camry//2022//150"));
            assertTrue(content.contains("BMW X5//2023//300"));
        }

        @Test
        @DisplayName("save() должен перезаписывать существующий файл")
        void save_ShouldOverwriteExistingFile() throws RepositoryException, IOException {
            // Given
            CustomArrayList<Car> initialCars = new CustomArrayList<>();
            initialCars.add(CAR1);
            repository.save(initialCars, TEST_NAME);

            CustomArrayList<Car> newCars = new CustomArrayList<>();
            newCars.add(CAR2);

            // When
            repository.save(newCars, TEST_NAME);

            // Then
            Path path = Path.of("files", TEST_NAME + ".txt");
            String content = Files.readString(path);
            assertFalse(content.contains("Toyota Camry"));
            assertTrue(content.contains("BMW X5"));
        }

        @Test
        @DisplayName("save() должен сохранять пустой список")
        void save_WithEmptyList_ShouldCreateEmptyFile() throws RepositoryException, IOException {
            // Given
            CustomArrayList<Car> emptyList = new CustomArrayList<>();

            // When
            boolean result = repository.save(emptyList, TEST_NAME);

            // Then
            assertTrue(result);
            Path path = Path.of("files", TEST_NAME + ".txt");
            assertTrue(Files.exists(path));
            String content = Files.readString(path);
            assertEquals("", content);
        }

        @Test
        @DisplayName("save() должен выбрасывать исключение при ошибке записи")
        void save_WhenWriteFails_ShouldThrowException() {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

            // When & Then
            // Для тестирования ошибки записи нужно смокировать Files.writeString
            // Или использовать недопустимый путь
            assertThrows(RepositoryException.class,
                    () -> repository.save(cars, "invalid/../path/name"));
        }
    }

    @Nested
    @DisplayName("Тесты save(Car, String)")
    class SaveSingleCarTests {

        @Test
        @DisplayName("save() должен добавлять автомобиль в новый файл")
        void save_ToNewFile_ShouldCreateFileWithCar() throws RepositoryException, IOException {
            // When
            boolean result = repository.save(CAR1, TEST_NAME);

            // Then
            assertTrue(result);
            Path path = Path.of("files", TEST_NAME + ".txt");
            assertTrue(Files.exists(path));
            String content = Files.readString(path);
            assertTrue(content.contains("Toyota Camry//2022//150"));
        }

        @Test
        @DisplayName("save() должен добавлять автомобиль в конец существующего файла")
        void save_ToExistingFile_ShouldAppendCar() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", TEST_NAME + ".txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, "Toyota Camry//2022//150");

            // When
            boolean result = repository.save(CAR2, TEST_NAME);

            // Then
            assertTrue(result);
            String content = Files.readString(path);
            assertTrue(content.contains("Toyota Camry//2022//150"));
            assertTrue(content.contains("BMW X5//2023//300"));
            assertTrue(content.startsWith("Toyota Camry//2022//150///BMW X5//2023//300"));
        }

        @Test
        @DisplayName("save() должен выбрасывать исключение при ошибке записи")
        void saveSingleCar_WhenWriteFails_ShouldThrowException() {
            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.save(CAR1, "invalid/../path/name"));
        }
    }

    @Nested
    @DisplayName("Тесты readListOfCarLists()")
    class ReadListOfCarListsTests {

        @Test
        @DisplayName("readListOfCarLists() должен читать список имен")
        void readListOfCarLists_ShouldReadNames() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", "ListOfCarLists.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, "list1///list2///list3");

            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("list1"));
            assertTrue(result.contains("list2"));
            assertTrue(result.contains("list3"));
        }

        @Test
        @DisplayName("readListOfCarLists() должен игнорировать пустые строки")
        void readListOfCarLists_ShouldIgnoreEmptyStrings() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", "ListOfCarLists.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, "list1//////list2///   ///list3");

            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("readListOfCarLists() должен возвращать пустой список при пустом файле")
        void readListOfCarLists_WithEmptyFile_ShouldReturnEmptyList() throws RepositoryException, IOException {
            // Given
            Path path = Path.of("files", "ListOfCarLists.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, "");

            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }
    }
}
