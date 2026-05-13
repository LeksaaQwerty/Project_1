package test;

import model.Car;
import org.junit.jupiter.api.*;
import repository.Repository;
import repository.RepositoryException;
import utils.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты интерфейса Repository")
class RepositoryTest {

    // Тестовая реализация интерфейса
    private static class TestRepository implements Repository {
        private CustomArrayList<Car> storedCars = new CustomArrayList<>();
        private CustomArrayList<String> storedListNames = new CustomArrayList<>();
        private boolean shouldThrowException = false;

        public TestRepository() {
            this(false);
        }

        public TestRepository(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        @Override
        public CustomArrayList<Car> readAll(int size, String name) throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка чтения");
            }
            if (name == null || name.isEmpty()) {
                throw new RepositoryException("Имя не может быть пустым");
            }
            CustomArrayList<Car> result = new CustomArrayList<>();
            int limit = Math.min(size, storedCars.size());
            for (int i = 0; i < limit; i++) {
                result.add(storedCars.get(i));
            }
            return result;
        }

        @Override
        public boolean save(CustomArrayList<Car> list, String name) throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка сохранения списка");
            }
            if (list == null || name == null || name.isEmpty()) {
                return false;
            }
            storedCars = list;
            return true;
        }

        @Override
        public boolean save(Car car, String name) throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка сохранения автомобиля");
            }
            if (car == null || name == null || name.isEmpty()) {
                return false;
            }
            storedCars.add(car);
            return true;
        }

        @Override
        public CustomArrayList<String> readListOfCarLists() throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка чтения списка имен");
            }
            return storedListNames;
        }

        @Override
        public boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка сохранения списка имен");
            }
            if (carListNames == null) {
                return false;
            }
            this.storedListNames = carListNames;
            return true;
        }

        @Override
        public CustomArrayList<String> getCarFilesList() throws RepositoryException {
            if (shouldThrowException) {
                throw new RepositoryException("Ошибка получения списка файлов");
            }
            return storedListNames;
        }

        public void setStoredCars(CustomArrayList<Car> cars) {
            this.storedCars = cars;
        }
    }

    private static final Car CAR1 = new Car.Builder("Toyota Camry")
            .yearOfProduction(2022)
            .hp(150)
            .build();

    private static final Car CAR2 = new Car.Builder("BMW X5")
            .yearOfProduction(2023)
            .hp(300)
            .build();

    @Nested
    @DisplayName("Тесты метода readAll()")
    class ReadAllTests {

        @Test
        @DisplayName("readAll() должен возвращать список автомобилей")
        void readAll_ShouldReturnCarList() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            repository.setStoredCars(cars);

            // When
            CustomArrayList<Car> result = repository.readAll(10, "test");

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(CAR1, result.get(0));
            assertEquals(CAR2, result.get(1));
        }

        @Test
        @DisplayName("readAll() должен ограничивать количество по size")
        void readAll_ShouldLimitBySize() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);
            repository.setStoredCars(cars);

            // When
            CustomArrayList<Car> result = repository.readAll(1, "test");

            // Then
            assertEquals(1, result.size());
            assertEquals(CAR1, result.get(0));
        }

        @Test
        @DisplayName("readAll() с size = 0 должен возвращать пустой список")
        void readAll_WithZeroSize_ShouldReturnEmptyList() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            CustomArrayList<Car> result = repository.readAll(0, "test");

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("readAll() должен выбрасывать RepositoryException при ошибке")
        void readAll_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.readAll(10, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода save(CustomArrayList)")
    class SaveListTests {

        @Test
        @DisplayName("save(CustomArrayList) должен сохранять список автомобилей")
        void saveList_ShouldSaveCarList() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);

            // When
            boolean result = repository.save(cars, "testList");

            // Then
            assertTrue(result);
            CustomArrayList<Car> readCars = repository.readAll(10, "testList");
            assertEquals(2, readCars.size());
        }

        @Test
        @DisplayName("save(CustomArrayList) с пустым именем должен возвращать false")
        void saveList_WithEmptyName_ShouldReturnFalse() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

            // When
            boolean result = repository.save(cars, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save(CustomArrayList) должен выбрасывать RepositoryException при ошибке")
        void saveList_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.save(cars, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода save(Car)")
    class SaveCarTests {

        @Test
        @DisplayName("save(Car) должен сохранять один автомобиль")
        void saveCar_ShouldSaveSingleCar() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            boolean result = repository.save(CAR1, "test");

            // Then
            assertTrue(result);
            CustomArrayList<Car> readCars = repository.readAll(10, "test");
            assertEquals(1, readCars.size());
            assertEquals(CAR1, readCars.get(0));
        }

        @Test
        @DisplayName("save(Car) должен добавлять автомобиль к существующим")
        void saveCar_ShouldAppendToExisting() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            repository.save(CAR1, "test");

            // When
            boolean result = repository.save(CAR2, "test");

            // Then
            assertTrue(result);
            CustomArrayList<Car> readCars = repository.readAll(10, "test");
            assertEquals(2, readCars.size());
            assertEquals(CAR1, readCars.get(0));
            assertEquals(CAR2, readCars.get(1));
        }

        @Test
        @DisplayName("save(Car) с null автомобилем должен возвращать false")
        void saveCar_WithNullCar_ShouldReturnFalse() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            boolean result = repository.save((Car) null, "test");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save(Car) с пустым именем должен возвращать false")
        void saveCar_WithEmptyName_ShouldReturnFalse() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            boolean result = repository.save(CAR1, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save(Car) должен выбрасывать RepositoryException при ошибке")
        void saveCar_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.save(CAR1, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода readListOfCarLists()")
    class ReadListOfCarListsTests {

        @Test
        @DisplayName("readListOfCarLists() должен возвращать список имен")
        void readListOfCarLists_ShouldReturnListOfNames() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<String> expectedNames = new CustomArrayList<>();
            expectedNames.add("list1");
            expectedNames.add("list2");
            repository.saveCarListNames(expectedNames);

            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("list1", result.get(0));
            assertEquals("list2", result.get(1));
        }

        @Test
        @DisplayName("readListOfCarLists() должен возвращать пустой список если нет имен")
        void readListOfCarLists_WhenNoNames_ShouldReturnEmptyList() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("readListOfCarLists() должен выбрасывать RepositoryException при ошибке")
        void readListOfCarLists_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.readListOfCarLists());
        }
    }

    @Nested
    @DisplayName("Тесты метода saveCarListNames()")
    class SaveCarListNamesTests {

        @Test
        @DisplayName("saveCarListNames() должен сохранять список имен")
        void saveCarListNames_ShouldSaveNames() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("summer2024");
            names.add("winter2024");

            // When
            boolean result = repository.saveCarListNames(names);

            // Then
            assertTrue(result);
            CustomArrayList<String> readNames = repository.readListOfCarLists();
            assertEquals(2, readNames.size());
        }

        @Test
        @DisplayName("saveCarListNames() с пустым списком должен возвращать true")
        void saveCarListNames_WithEmptyList_ShouldReturnTrue() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<String> emptyList = new CustomArrayList<>();

            // When
            boolean result = repository.saveCarListNames(emptyList);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("saveCarListNames() с null списком должен возвращать false")
        void saveCarListNames_WithNullList_ShouldReturnFalse() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            boolean result = repository.saveCarListNames(null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveCarListNames() должен выбрасывать RepositoryException при ошибке")
        void saveCarListNames_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("test");

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.saveCarListNames(names));
        }
    }

    @Nested
    @DisplayName("Тесты метода getCarFilesList()")
    class GetCarFilesListTests {

        @Test
        @DisplayName("getCarFilesList() должен возвращать список файлов")
        void getCarFilesList_ShouldReturnListOfFiles() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();
            CustomArrayList<String> expectedFiles = new CustomArrayList<>();
            expectedFiles.add("file1.txt");
            expectedFiles.add("file2.txt");
            repository.saveCarListNames(expectedFiles);

            // When
            CustomArrayList<String> result = repository.getCarFilesList();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("file1.txt", result.get(0));
            assertEquals("file2.txt", result.get(1));
        }

        @Test
        @DisplayName("getCarFilesList() должен возвращать пустой список если нет файлов")
        void getCarFilesList_WhenNoFiles_ShouldReturnEmptyList() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // When
            CustomArrayList<String> result = repository.getCarFilesList();

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("getCarFilesList() должен выбрасывать RepositoryException при ошибке")
        void getCarFilesList_WhenError_ShouldThrowException() {
            // Given
            TestRepository repository = new TestRepository(true);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.getCarFilesList());
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Полный цикл работы с репозиторием")
        void completeWorkflow_ShouldWorkCorrectly() throws RepositoryException {
            // Given
            TestRepository repository = new TestRepository();

            // 1. Сохраняем автомобиль
            boolean saveCarResult = repository.save(CAR1, "myCars");
            assertTrue(saveCarResult);

            // 2. Сохраняем список автомобилей
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR2);
            boolean saveListResult = repository.save(cars, "myCars");
            assertTrue(saveListResult);

            // 3. Сохраняем список имен
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("myCars");
            boolean saveNamesResult = repository.saveCarListNames(names);
            assertTrue(saveNamesResult);

            // 4. Получаем список имен
            CustomArrayList<String> retrievedNames = repository.readListOfCarLists();
            assertNotNull(retrievedNames);

            // 5. Получаем список файлов
            CustomArrayList<String> files = repository.getCarFilesList();
            assertNotNull(files);

            // 6. Читаем автомобили
            CustomArrayList<Car> readCars = repository.readAll(10, "myCars");
            assertNotNull(readCars);
        }
    }
}
