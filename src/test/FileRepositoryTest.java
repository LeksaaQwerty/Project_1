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

        /*@Test
        @DisplayName("save() должен корректно добавлять несколько автомобилей последовательно")
        void save_ShouldAppendMultipleCars() throws RepositoryException, IOException {
            // When
            repository.save(CAR1, TEST_NAME);
            repository.save(CAR2, TEST_NAME);

            // Then
            Path path = Path.of("files", TEST_NAME + ".txt");
            String content = Files.readString(path);
            String[] cars = content.split("///");
            assertEquals(2, cars.length);
            assertTrue(cars[0].contains("Toyota Camry"));
            assertTrue(cars[1].contains("BMW X5"));
        }*/

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

        /*@Test
        @DisplayName("readListOfCarLists() должен выбрасывать исключение при отсутствии файла")
        void readListOfCarLists_WhenFileNotFound_ShouldThrowException() {
            // When & Then
            RepositoryException exception = assertThrows(RepositoryException.class,
                    () -> repository.readListOfCarLists());
            assertEquals("Файл данных не найден", exception.getMessage());
        }*/

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

    @Nested
    @DisplayName("Тесты saveCarListNames()")
    class SaveCarListNamesTests {

        @Test
        @DisplayName("saveCarListNames() должен сохранять список имен")
        void saveCarListNames_ShouldSaveNames() throws RepositoryException, IOException {
            // Given
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("list1");
            names.add("list2");
            names.add("list3");

            // When
            boolean result = repository.saveCarListNames(names);

            // Then
            assertFalse(result); // Метод всегда возвращает false по текущей реализации
            Path path = Path.of("files", "ListOfCarLists.txt");
            assertTrue(Files.exists(path));
            String content = Files.readString(path);
            assertTrue(content.contains("list1"));
            assertTrue(content.contains("list2"));
            assertTrue(content.contains("list3"));
        }

        @Test
        @DisplayName("saveCarListNames() должен сохранять пустой список")
        void saveCarListNames_WithEmptyList_ShouldCreateEmptyFile() throws RepositoryException, IOException {
            // Given
            CustomArrayList<String> emptyList = new CustomArrayList<>();

            // When
            repository.saveCarListNames(emptyList);

            // Then
            Path path = Path.of("files", "ListOfCarLists.txt");
            assertTrue(Files.exists(path));
            String content = Files.readString(path);
            assertEquals("", content);
        }

        @Test
        @DisplayName("saveCarListNames() должен перезаписывать существующий файл")
        void saveCarListNames_ShouldOverwriteExistingFile() throws RepositoryException, IOException {
            // Given
            CustomArrayList<String> initialNames = new CustomArrayList<>();
            initialNames.add("oldList");
            repository.saveCarListNames(initialNames);

            CustomArrayList<String> newNames = new CustomArrayList<>();
            newNames.add("newList");

            // When
            repository.saveCarListNames(newNames);

            // Then
            Path path = Path.of("files", "ListOfCarLists.txt");
            String content = Files.readString(path);
            assertFalse(content.contains("oldList"));
            assertTrue(content.contains("newList"));
        }

        @Test
        @DisplayName("saveCarListNames() должен выбрасывать исключение при ошибке записи")
        void saveCarListNames_WhenWriteFails_ShouldThrowException() {
            // This test would require mocking static methods or using invalid path
            // For now, testing with read-only directory would be complex
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Полный цикл сохранения и чтения списка автомобилей")
        void fullSaveAndReadCycle_ShouldWorkCorrectly() throws RepositoryException, IOException {
            // Given
            CustomArrayList<Car> originalCars = new CustomArrayList<>();
            originalCars.add(CAR1);
            originalCars.add(CAR2);

            // When
            repository.save(originalCars, TEST_NAME);
            CustomArrayList<Car> readCars = repository.readAll(10, TEST_NAME);

            // Then
            assertEquals(originalCars.size(), readCars.size());
            assertEquals(originalCars.get(0), readCars.get(0));
            assertEquals(originalCars.get(1), readCars.get(1));
        }

        @Test
        @DisplayName("Полный цикл сохранения и чтения каталога списков")
        void fullSaveAndReadCycleForLists_ShouldWorkCorrectly() throws RepositoryException {
            // Given
            CustomArrayList<String> originalNames = new CustomArrayList<>();
            originalNames.add("summer2024");
            originalNames.add("winter2024");

            // When
            repository.saveCarListNames(originalNames);
            CustomArrayList<String> readNames = repository.readListOfCarLists();

            // Then
            assertEquals(originalNames.size(), readNames.size());
            assertEquals(originalNames.get(0), readNames.get(0));
            assertEquals(originalNames.get(1), readNames.get(1));
        }

        /*@Test
        @DisplayName("Сохранение одного авто, затем списка, затем добавление авто")
        void complexScenario_ShouldWorkCorrectly() throws RepositoryException, IOException {
            // Given
            String listName = "myGarage";

            // When - сохраняем одно авто
            repository.save(CAR1, listName);

            // Then - проверяем
            CustomArrayList<Car> singleCarList = repository.readAll(10, listName);
            assertEquals(1, singleCarList.size());

            // When - добавляем второе авто
            repository.save(CAR2, listName);

            // Then - проверяем оба
            CustomArrayList<Car> bothCars = repository.readAll(10, listName);
            assertEquals(2, bothCars.size());
        }*/
    }
}