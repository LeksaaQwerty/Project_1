package test;

import model.Car;
import org.junit.jupiter.api.*;
import service.CarService;
import service.ServiceException;
import utils.CustomArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты интерфейса CarService")
class CarServiceTest {

    // Тестовая реализация интерфейса
    private static class TestCarService implements CarService {
        private final CustomArrayList<String> listNames;
        private final boolean shouldThrowException;
        private CustomArrayList<Car> lastSavedList = null;
        private Car lastSavedCar = null;
        private String lastUsedName = null;

        TestCarService(CustomArrayList<String> listNames, boolean shouldThrowException) {
            this.listNames = listNames;
            this.shouldThrowException = shouldThrowException;
        }

        @Override
        public CustomArrayList<String> getListNames() throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка получения списка имен");
            }
            return listNames;
        }

        @Override
        public boolean saveAll(CustomArrayList<Car> carList, String nameList) throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка сохранения списка");
            }
            if (carList == null || nameList == null || nameList.isEmpty()) {
                return false;
            }
            lastSavedList = carList;
            lastUsedName = nameList;
            return true;
        }

        @Override
        public boolean save(Car car, String nameList) throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка сохранения автомобиля");
            }
            if (car == null || nameList == null || nameList.isEmpty()) {
                return false;
            }
            lastSavedCar = car;
            lastUsedName = nameList;
            return true;
        }

        @Override
        public boolean saveCarListNames(CustomArrayList<String> carListNames) throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка сохранения списка имен");
            }
            if (carListNames == null) {
                return false;
            }
            return true;
        }

        public CustomArrayList<Car> getLastSavedList() { return lastSavedList; }
        public Car getLastSavedCar() { return lastSavedCar; }
        public String getLastUsedName() { return lastUsedName; }
    }

    private static final Car TEST_CAR = new Car.Builder("Toyota Camry")
            .yearOfProduction(2022)
            .hp(150)
            .build();

    private static final Car TEST_CAR2 = new Car.Builder("BMW X5")
            .yearOfProduction(2023)
            .hp(300)
            .build();

    @Nested
    @DisplayName("Тесты метода getListNames()")
    class GetListNamesTests {

        @Test
        @DisplayName("getListNames() должен возвращать список имен")
        void getListNames_ShouldReturnListOfNames() throws ServiceException {
            // Given
            CustomArrayList<String> expectedNames = new CustomArrayList<>();
            expectedNames.add("list1");
            expectedNames.add("list2");
            CarService service = new TestCarService(expectedNames, false);

            // When
            CustomArrayList<String> result = service.getListNames();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("list1", result.get(0));
            assertEquals("list2", result.get(1));
        }

        @Test
        @DisplayName("getListNames() должен возвращать пустой список, если нет имен")
        void getListNames_WithNoNames_ShouldReturnEmptyList() throws ServiceException {
            // Given
            CustomArrayList<String> emptyList = new CustomArrayList<>();
            CarService service = new TestCarService(emptyList, false);

            // When
            CustomArrayList<String> result = service.getListNames();

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("getListNames() должен выбрасывать ServiceException при ошибке")
        void getListNames_WhenError_ShouldThrowException() {
            // Given
            CarService service = new TestCarService(new CustomArrayList<>(), true);

            // When & Then
            assertThrows(ServiceException.class, () -> service.getListNames());
        }
    }

    @Nested
    @DisplayName("Тесты метода saveAll()")
    class SaveAllTests {

        @Test
        @DisplayName("saveAll() должен сохранять список автомобилей")
        void saveAll_ShouldSaveCarList() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR);
            cars.add(TEST_CAR2);

            // When
            boolean result = service.saveAll(cars, "myCars");

            // Then
            assertTrue(result);
            assertNotNull(service.getLastSavedList());
            assertEquals(2, service.getLastSavedList().size());
            assertEquals("myCars", service.getLastUsedName());
        }

        @Test
        @DisplayName("saveAll() с null списком должен возвращать false")
        void saveAll_WithNullList_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.saveAll(null, "test");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveAll() с пустым именем должен возвращать false")
        void saveAll_WithEmptyName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR);

            // When
            boolean result = service.saveAll(cars, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveAll() должен выбрасывать ServiceException при ошибке")
        void saveAll_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), true);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR);

            // When & Then
            assertThrows(ServiceException.class, () -> service.saveAll(cars, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода save()")
    class SaveTests {

        @Test
        @DisplayName("save() должен сохранять один автомобиль")
        void save_ShouldSaveSingleCar() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.save(TEST_CAR, "myGarage");

            // Then
            assertTrue(result);
            assertEquals(TEST_CAR, service.getLastSavedCar());
            assertEquals("myGarage", service.getLastUsedName());
        }

        @Test
        @DisplayName("save() с null автомобилем должен возвращать false")
        void save_WithNullCar_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.save(null, "test");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() с пустым именем должен возвращать false")
        void save_WithEmptyName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.save(TEST_CAR, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() с null именем должен возвращать false")
        void save_WithNullName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.save(TEST_CAR, null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() должен выбрасывать ServiceException при ошибке")
        void save_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), true);

            // When & Then
            assertThrows(ServiceException.class, () -> service.save(TEST_CAR, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода saveCarListNames()")
    class SaveCarListNamesTests {

        @Test
        @DisplayName("saveCarListNames() должен сохранять список имен")
        void saveCarListNames_ShouldSaveNames() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("summer2024");
            names.add("winter2024");

            // When
            boolean result = service.saveCarListNames(names);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("saveCarListNames() с пустым списком должен возвращать true")
        void saveCarListNames_WithEmptyList_ShouldReturnTrue() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);
            CustomArrayList<String> emptyList = new CustomArrayList<>();

            // When
            boolean result = service.saveCarListNames(emptyList);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("saveCarListNames() с null списком должен возвращать false")
        void saveCarListNames_WithNullList_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // When
            boolean result = service.saveCarListNames(null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveCarListNames() должен выбрасывать ServiceException при ошибке")
        void saveCarListNames_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), true);
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("test");

            // When & Then
            assertThrows(ServiceException.class, () -> service.saveCarListNames(names));
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Полный цикл работы с сервисом")
        void completeWorkflow_ShouldWorkCorrectly() throws ServiceException {
            // Given
            TestCarService service = new TestCarService(new CustomArrayList<>(), false);

            // 1. Сохраняем автомобиль
            boolean saveResult = service.save(TEST_CAR, "myCars");
            assertTrue(saveResult);

            // 2. Сохраняем список автомобилей
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(TEST_CAR2);
            boolean saveAllResult = service.saveAll(cars, "myCars");
            assertTrue(saveAllResult);

            // 3. Сохраняем список имен
            CustomArrayList<String> names = new CustomArrayList<>();
            names.add("myCars");
            boolean saveNamesResult = service.saveCarListNames(names);
            assertTrue(saveNamesResult);

            // 4. Получаем список имен
            CustomArrayList<String> retrievedNames = service.getListNames();
            assertNotNull(retrievedNames);
        }
    }
}