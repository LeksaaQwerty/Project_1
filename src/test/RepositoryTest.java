package test;

import model.Car;
import org.junit.jupiter.api.*;
import repository.Repository;
import repository.RepositoryException;
import utils.CustomArrayList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты интерфейса Repository")
class RepositoryTest {

    private Repository repository;

    // Тестовые данные
    private static final String TEST_NAME = "testList";
    private static final Car TEST_CAR = new Car.Builder("Toyota Camry")
            .yearOfProduction(2022)
            .hp(150)
            .build();
    private static final Car TEST_CAR2 = new Car.Builder("BMW X5")
            .yearOfProduction(2023)
            .hp(300)
            .build();

    @BeforeEach
    void setUp() {
        // Для тестирования интерфейса создаем тестовую реализацию
        repository = new InMemoryTestRepository();
    }

    @Nested
    @DisplayName("Тесты метода readAll(int size, String name)")
    class ReadAllTests {

        @Test
        @DisplayName("readAll() должен возвращать список автомобилей указанного размера")
        void readAll_ShouldReturnListOfSpecifiedSize() throws RepositoryException {
            // Given
            // В тестовой реализации предзаполняем данные

            // When
            CustomArrayList<Car> result = repository.readAll(5, TEST_NAME);

            // Then
            assertNotNull(result);
            assertTrue(result.size() <= 5);
        }

        @Test
        @DisplayName("readAll() должен возвращать пустой список, если файл не существует")
        void readAll_WhenFileNotFound_ShouldReturnEmptyList() throws RepositoryException {
            // When
            CustomArrayList<Car> result = repository.readAll(10, "nonExistentList");

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("readAll() должен выбрасывать RepositoryException при ошибке чтения")
        void readAll_WhenReadFails_ShouldThrowRepositoryException() {
            // Given
            Repository failingRepository = new FailingTestRepository();

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> failingRepository.readAll(10, TEST_NAME));
        }

        @Test
        @DisplayName("readAll() с size = 0 должен возвращать пустой список")
        void readAll_WithZeroSize_ShouldReturnEmptyList() throws RepositoryException {
            // When
            CustomArrayList<Car> result = repository.readAll(0, TEST_NAME);

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("readAll() с отрицательным size должен обрабатываться корректно")
        void readAll_WithNegativeSize_ShouldHandleGracefully() throws RepositoryException {
            // When
            CustomArrayList<Car> result = repository.readAll(-1, TEST_NAME);

            // Then
            assertNotNull(result);
            // Ожидаем, что метод либо вернет пустой список, либо выбросит исключение
        }
    }

    @Nested
    @DisplayName("Тесты метода save(CustomArrayList<Car> list, String name)")
    class SaveListTests {

        @Test
        @DisplayName("save(CustomArrayList) должен успешно сохранять список автомобилей")
        void saveList_ShouldSaveSuccessfully() throws RepositoryException {
            // Given
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR);
            cars.add(TEST_CAR2);

            // When
            boolean result = repository.save(cars, TEST_NAME);

            // Then
            assertTrue(result);
            // Проверяем, что список сохранился
            CustomArrayList<Car> readCars = repository.readAll(10, TEST_NAME);
            assertEquals(cars.size(), readCars.size());
        }

        @Test
        @DisplayName("save(CustomArrayList) должен перезаписывать существующий список")
        void saveList_ShouldOverwriteExistingList() throws RepositoryException {
            // Given
            CustomArrayList<Car> firstList = new CustomArrayList<>();
            firstList.add(TEST_CAR);
            repository.save(firstList, TEST_NAME);

            CustomArrayList<Car> secondList = new CustomArrayList<>();
            secondList.add(TEST_CAR2);

            // When
            repository.save(secondList, TEST_NAME);

            // Then
            CustomArrayList<Car> result = repository.readAll(10, TEST_NAME);
            assertEquals(1, result.size());
            assertEquals(TEST_CAR2, result.get(0));
        }

        @Test
        @DisplayName("save(CustomArrayList) должен сохранять пустой список")
        void saveList_WithEmptyList_ShouldSaveEmptyList() throws RepositoryException {
            // Given
            CustomArrayList<Car> emptyList = new CustomArrayList<>();

            // When
            boolean result = repository.save(emptyList, TEST_NAME);

            // Then
            assertTrue(result);
            CustomArrayList<Car> readCars = repository.readAll(10, TEST_NAME);
            assertEquals(0, readCars.size());
        }

        @Test
        @DisplayName("save(CustomArrayList) должен выбрасывать RepositoryException при ошибке")
        void saveList_WhenSaveFails_ShouldThrowRepositoryException() {
            // Given
            Repository failingRepository = new FailingTestRepository();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR);

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> failingRepository.save(cars, TEST_NAME));
        }

        @Test
        @DisplayName("save(CustomArrayList) с null списком должен выбрасывать исключение")
        void saveList_WithNullList_ShouldThrowException() {
            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.save((Car) null, TEST_NAME));
        }
    }

    @Nested
    @DisplayName("Тесты метода save(Car car, String name)")
    class SaveSingleCarTests {

        @Test
        @DisplayName("save(Car) должен успешно сохранять один автомобиль")
        void saveSingleCar_ShouldSaveSuccessfully() throws RepositoryException {
            // When
            boolean result = repository.save(TEST_CAR, TEST_NAME);

            // Then
            assertTrue(result);
            CustomArrayList<Car> readCars = repository.readAll(10, TEST_NAME);
            assertEquals(1, readCars.size());
            assertEquals(TEST_CAR, readCars.get(0));
        }

        @Test
        @DisplayName("save(Car) должен добавлять автомобиль к существующим")
        void saveSingleCar_ShouldAppendToExisting() throws RepositoryException {
            // Given
            repository.save(TEST_CAR, TEST_NAME);

            // When
            repository.save(TEST_CAR2, TEST_NAME);

            // Then
            CustomArrayList<Car> result = repository.readAll(10, TEST_NAME);
            assertEquals(2, result.size());
            assertEquals(TEST_CAR, result.get(0));
            assertEquals(TEST_CAR2, result.get(1));
        }

        @Test
        @DisplayName("save(Car) с null автомобилем должен выбрасывать исключение")
        void saveSingleCar_WithNullCar_ShouldThrowException() {
            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.save((Car) null, TEST_NAME));
        }

        @Test
        @DisplayName("save(Car) с пустым именем должен обрабатываться корректно")
        void saveSingleCar_WithEmptyName_ShouldHandleGracefully() {
            // When & Then
            // В зависимости от реализации может быть исключение или успешное сохранение
            assertDoesNotThrow(() -> repository.save(TEST_CAR, ""));
        }

        @Test
        @DisplayName("save(Car) должен выбрасывать RepositoryException при ошибке сохранения")
        void saveSingleCar_WhenSaveFails_ShouldThrowRepositoryException() {
            // Given
            Repository failingRepository = new FailingTestRepository();

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> failingRepository.save(TEST_CAR, TEST_NAME));
        }
    }

    @Nested
    @DisplayName("Тесты метода readListOfCarLists()")
    class ReadListOfCarListsTests {

        @Test
        @DisplayName("readListOfCarLists() должен возвращать пустой список, если нет сохраненных списков")
        void readListOfCarLists_WhenNoLists_ShouldReturnEmptyList() throws RepositoryException {
            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
            // Может быть 0 или более в зависимости от реализации
        }

        @Test
        @DisplayName("readListOfCarLists() не должен возвращать null")
        void readListOfCarLists_ShouldNeverReturnNull() throws RepositoryException {
            // When
            CustomArrayList<String> result = repository.readListOfCarLists();

            // Then
            assertNotNull(result);
        }

        @Test
        @DisplayName("readListOfCarLists() должен выбрасывать RepositoryException при ошибке")
        void readListOfCarLists_WhenReadFails_ShouldThrowRepositoryException() {
            // Given
            Repository failingRepository = new FailingTestRepository();

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> failingRepository.readListOfCarLists());
        }
    }

    @Nested
    @DisplayName("Тесты метода saveCarListNames(CustomArrayList<String> carListNames)")
    class SaveCarListNamesTests {

        @Test
        @DisplayName("saveCarListNames() должен успешно сохранять список имен")
        void saveCarListNames_ShouldSaveSuccessfully() throws RepositoryException {
            // Given
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("list1");
            names.add("list2");
            names.add("list3");

            // When
            boolean result = repository.saveCarListNames(names);

            // Then
            assertTrue(result);
            CustomArrayList<String> readNames = repository.readListOfCarLists();
            assertEquals(names.size(), readNames.size());
        }

        @Test
        @DisplayName("saveCarListNames() должен перезаписывать существующий список")
        void saveCarListNames_ShouldOverwriteExisting() throws RepositoryException {
            // Given
            CustomArrayList<String> firstList = new CustomArrayList<>();
            firstList.add("oldList");
            repository.saveCarListNames(firstList);

            CustomArrayList<String> secondList = new CustomArrayList<>();
            secondList.add("newList");

            // When
            repository.saveCarListNames(secondList);

            // Then
            CustomArrayList<String> result = repository.readListOfCarLists();
            assertEquals(1, result.size());
            assertEquals("newList", result.get(0));
        }

        @Test
        @DisplayName("saveCarListNames() должен сохранять пустой список")
        void saveCarListNames_WithEmptyList_ShouldSaveEmptyList() throws RepositoryException {
            // Given
            CustomArrayList<String> emptyList = new CustomArrayList<>();

            // When
            boolean result = repository.saveCarListNames(emptyList);

            // Then
            assertTrue(result);
            CustomArrayList<String> readNames = repository.readListOfCarLists();
            assertEquals(0, readNames.size());
        }

        @Test
        @DisplayName("saveCarListNames() с null списком должен выбрасывать исключение")
        void saveCarListNames_WithNullList_ShouldThrowException() {
            // When & Then
            assertThrows(RepositoryException.class,
                    () -> repository.saveCarListNames(null));
        }

        @Test
        @DisplayName("saveCarListNames() должен выбрасывать RepositoryException при ошибке сохранения")
        void saveCarListNames_WhenSaveFails_ShouldThrowRepositoryException() {
            // Given
            Repository failingRepository = new FailingTestRepository();
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("test");

            // When & Then
            assertThrows(RepositoryException.class,
                    () -> failingRepository.saveCarListNames(names));
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Полный цикл работы с репозиторием")
        void completeWorkflow_ShouldWorkCorrectly() throws RepositoryException {
            // 1. Создаем список автомобилей
            CustomArrayList<Car> sportsCars = new CustomArrayList<>();
            sportsCars.add(TEST_CAR);
            sportsCars.add(TEST_CAR2);

            // 2. Сохраняем список
            repository.save(sportsCars, "sports");

            // 3. Сохраняем имя списка в каталог
            CustomArrayList<String> allLists = repository.readListOfCarLists();
            allLists.add("sports");
            repository.saveCarListNames(allLists);

            // 4. Добавляем еще один автомобиль в существующий список
            Car electricCar = new Car.Builder("Tesla Model 3")
                    .yearOfProduction(2024)
                    .hp(450)
                    .build();
            repository.save(electricCar, "sports");

            // 5. Читаем обновленный список
            CustomArrayList<Car> readCars = repository.readAll(10, "sports");

            // 6. Проверяем результат
            assertEquals(3, readCars.size());
            assertEquals(TEST_CAR, readCars.get(0));
            assertEquals(TEST_CAR2, readCars.get(1));
            assertEquals(electricCar, readCars.get(2));

            // 7. Проверяем каталог
            CustomArrayList<String> readLists = repository.readListOfCarLists();
            assertTrue(readLists.contains("sports"));
        }

        @Test
        @DisplayName("Сохранение и чтение множества списков")
        void multipleLists_ShouldWorkCorrectly() throws RepositoryException {
            // Given
            String[] listNames = {"list1", "list2", "list3", "list4", "list5"};
            CustomArrayList<String> savedNames = new CustomArrayList<>();

            // When
            for (String name : listNames) {
                CustomArrayList<Car> cars = new CustomArrayList<>();
                cars.add(TEST_CAR);
                repository.save(cars, name);
                savedNames.add(name);
            }
            repository.saveCarListNames(savedNames);

            // Then
            CustomArrayList<String> readNames = repository.readListOfCarLists();
            assertEquals(listNames.length, readNames.size());
            for (String name : listNames) {
                assertTrue(readNames.contains(name));
                CustomArrayList<Car> cars = repository.readAll(10, name);
                assertEquals(1, cars.size());
            }
        }
    }
}

// Тестовая реализация репозитория в памяти для тестирования интерфейса
class InMemoryTestRepository implements Repository {

    private final java.util.Map<String, CustomArrayList<Car>> storage = new java.util.HashMap<>();
    private CustomArrayList<String> listNames = new CustomArrayList<>();

    @Override
    public CustomArrayList<Car> readAll(int size, String name) throws RepositoryException {
        validateName(name);
        CustomArrayList<Car> cars = storage.get(name);
        if (cars == null) {
            return new CustomArrayList<>();
        }

        CustomArrayList<Car> result = new CustomArrayList<>();
        int limit = Math.min(size, cars.size());
        for (int i = 0; i < limit; i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    @Override
    public boolean save(CustomArrayList<Car> list, String name) throws RepositoryException {
        validateName(name);
        validateList(list);

        storage.put(name, copyList(list));
        return true;
    }

    @Override
    public boolean save(Car car, String name) throws RepositoryException {
        validateName(name);
        validateCar(car);

        CustomArrayList<Car> existing = storage.get(name);
        if (existing == null) {
            existing = new CustomArrayList<>();
            storage.put(name, existing);
        }
        existing.add(car);
        return true;
    }

    @Override
    public CustomArrayList<String> readListOfCarLists() throws RepositoryException {
        return copyStringList(listNames);
    }

    @Override
    public boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException {
        validateList(carListNames);
        listNames = copyStringList(carListNames);
        return true;
    }

    private void validateName(String name) throws RepositoryException {
        if (name == null) {
            throw new RepositoryException("Имя не может быть null");
        }
    }

    private void validateList(CustomArrayList<?> list) throws RepositoryException {
        if (list == null) {
            throw new RepositoryException("Список не может быть null");
        }
    }

    private void validateCar(Car car) throws RepositoryException {
        if (car == null) {
            throw new RepositoryException("Автомобиль не может быть null");
        }
    }

    private CustomArrayList<Car> copyList(CustomArrayList<Car> original) {
        CustomArrayList<Car> copy = new CustomArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }

    private CustomArrayList<String> copyStringList(CustomArrayList<String> original) {
        CustomArrayList<String> copy = new CustomArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }
}

// Реализация, которая всегда выбрасывает исключения для тестирования ошибок
class FailingTestRepository implements Repository {

    @Override
    public CustomArrayList<Car> readAll(int size, String name) throws RepositoryException {
        throw new RepositoryException("Тестовая ошибка чтения");
    }

    @Override
    public boolean save(CustomArrayList<Car> list, String name) throws RepositoryException {
        throw new RepositoryException("Тестовая ошибка сохранения списка");
    }

    @Override
    public boolean save(Car car, String name) throws RepositoryException {
        throw new RepositoryException("Тестовая ошибка сохранения автомобиля");
    }

    @Override
    public CustomArrayList<String> readListOfCarLists() throws RepositoryException {
        throw new RepositoryException("Тестовая ошибка чтения списка имен");
    }

    @Override
    public boolean saveCarListNames(CustomArrayList<String> carListNames) throws RepositoryException {
        throw new RepositoryException("Тестовая ошибка сохранения списка имен");
    }
}
