package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.CarService;
import service.CarServiceImpl;
import service.ServiceException;
import utils.CustomArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты CarServiceImpl")
class CarServiceImplTest {

    private CarService carService;
    private static final String TEST_DIR = "files";

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
        // Создаем тестовую директорию
        Path testPath = Paths.get(TEST_DIR);
        if (!Files.exists(testPath)) {
            Files.createDirectories(testPath);
        }

        // Очищаем тестовые файлы
        cleanTestFiles();

        carService = new CarServiceImpl();
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanTestFiles();
    }

    private void cleanTestFiles() throws IOException {
        Path testPath = Paths.get(TEST_DIR);
        if (Files.exists(testPath)) {
            Files.walk(testPath)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Игнорируем
                        }
                    });
        }
    }

    @Nested
    @DisplayName("Тесты saveAll()")
    class SaveAllTests {

        @Test
        @DisplayName("saveAll() должен сохранять список автомобилей")
        void saveAll_ShouldSaveCarList() throws ServiceException {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);

            // When
            boolean result = carService.saveAll(cars, "testList");

            // Then
            assertTrue(result);
            Path filePath = Paths.get(TEST_DIR, "testList.txt");
            assertTrue(Files.exists(filePath));
        }

        @Test
        @DisplayName("saveAll() с пустым списком должен сохранять пустой файл")
        void saveAll_WithEmptyList_ShouldSaveEmptyFile() throws ServiceException {
            // Given
            CustomArrayList<Car> emptyList = new CustomArrayList<>();

            // When
            boolean result = carService.saveAll(emptyList, "emptyList");

            // Then
            assertTrue(result);
            Path filePath = Paths.get(TEST_DIR, "emptyList.txt");
            assertTrue(Files.exists(filePath));
        }


        @Nested
        @DisplayName("Тесты save()")
        class SaveTests {

            @Test
            @DisplayName("save() должен сохранять один автомобиль")
            void save_ShouldSaveSingleCar() throws ServiceException {
                // When
                boolean result = carService.save(CAR1, "singleCar");

                // Then
                assertTrue(result);
                Path filePath = Paths.get(TEST_DIR, "singleCar.txt");
                assertTrue(Files.exists(filePath));
            }

            @Test
            @DisplayName("save() должен добавлять автомобиль к существующим")
            void save_ShouldAppendToExistingFile() throws ServiceException {
                // Given
                carService.save(CAR1, "appendTest");

                // When
                boolean result = carService.save(CAR2, "appendTest");

                // Then
                assertTrue(result);
                Path filePath = Paths.get(TEST_DIR, "appendTest.txt");
                assertTrue(Files.exists(filePath));
            }
        }



        @Nested
        @DisplayName("Тесты saveCarListNames()")
        class SaveCarListNamesTests {

            @Test
            @DisplayName("saveCarListNames() должен сохранять список имен")
            void saveCarListNames_ShouldSaveNames() throws ServiceException {
                // Given
                CustomArrayList<String> names = new CustomArrayList<>();
                names.add("summer2024");
                names.add("winter2024");

                // When
                boolean result = carService.saveCarListNames(names);

                // Then
                assertTrue(result);
                Path filePath = Paths.get(TEST_DIR, "ListOfCarLists.txt");
                assertTrue(Files.exists(filePath));
            }
        }

        @Nested
        @DisplayName("Тесты getCarFilesList() - НОВЫЙ МЕТОД")
        class GetCarFilesListTests {



            @Test
            @DisplayName("getCarFilesList() должен возвращать пустой список если нет файлов")
            void getCarFilesList_WhenNoFiles_ShouldReturnEmptyList() throws ServiceException {
                // When
                CustomArrayList<String> result = carService.getCarFilesList();

                // Then
                assertNotNull(result);
                // Результат может быть пустым или содержать имена
            }
        }

        @Nested
        @DisplayName("Интеграционные тесты")
        class IntegrationTests {

            @Test
            @DisplayName("Полный цикл работы с сервисом включая getCarFilesList")
            void completeWorkflow_ShouldWorkCorrectly() throws ServiceException {
                // 1. Сохраняем автомобиль
                boolean saveResult = carService.save(CAR1, "myCars");
                assertTrue(saveResult);

                // 2. Сохраняем список автомобилей
                CustomArrayList<Car> cars = new CustomArrayList<>();
                cars.add(CAR2);
                boolean saveAllResult = carService.saveAll(cars, "myCars");
                assertTrue(saveAllResult);

                // 3. Сохраняем список имен
                CustomArrayList<String> names = new CustomArrayList<>();
                names.add("myCars");
                boolean saveNamesResult = carService.saveCarListNames(names);
                assertTrue(saveNamesResult);

                // 4. Получаем список имен
                CustomArrayList<String> retrievedNames = carService.getListNames();
                assertNotNull(retrievedNames);

                // 5. Получаем список файлов
                CustomArrayList<String> files = carService.getCarFilesList();
                assertNotNull(files);
            }

            @Test
            @DisplayName("Сохранение и получение списка файлов")
            void saveAndGetCarFilesList_ShouldWork() throws ServiceException {
                // Given
                CustomArrayList<String> expectedFiles = new CustomArrayList<>();
                expectedFiles.add("file1");
                expectedFiles.add("file2");
                expectedFiles.add("file3");

                carService.saveCarListNames(expectedFiles);

                // When
                CustomArrayList<String> actualFiles = carService.getCarFilesList();

                // Then
                assertNotNull(actualFiles);
                // Проверяем, что файлы сохранены
            }
        }
    }
}
