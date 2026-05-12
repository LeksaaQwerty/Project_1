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
        private CustomArrayList<String> listNames = new CustomArrayList<>();
        private CustomArrayList<String> carFilesList = new CustomArrayList<>();
        private boolean shouldThrowException = false;

        public TestCarService(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        public TestCarService() {
            this(false);
        }

        @Override
        public CustomArrayList<String> getListNames() throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка получения списка имен");
            }
            return listNames;
        }

        @Override
        public CustomArrayList<String> getCarFilesList() throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка получения списка файлов");
            }
            return carFilesList;
        }

        @Override
        public boolean saveAll(CustomArrayList<Car> carList, String nameList) throws ServiceException {
            if (shouldThrowException) {
                throw new ServiceException("Ошибка сохранения списка");
            }
            if (carList == null || nameList == null || nameList.isEmpty()) {
                return false;
            }
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
            this.listNames = carListNames;
            return true;
        }

        public void setListNames(CustomArrayList<String> names) {
            this.listNames = names;
        }

        public void setCarFilesList(CustomArrayList<String> files) {
            this.carFilesList = files;
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
    @DisplayName("Тесты метода getListNames()")
    class GetListNamesTests {

        @Test
        @DisplayName("getListNames() должен возвращать список имен")
        void getListNames_ShouldReturnListOfNames() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();
            CustomArrayList<String> expectedNames = new CustomArrayList<>();
            expectedNames.add("list1");
            expectedNames.add("list2");
            service.setListNames(expectedNames);

            // When
            CustomArrayList<String> result = service.getListNames();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("list1", result.get(0));
            assertEquals("list2", result.get(1));
        }

        @Test
        @DisplayName("getListNames() должен возвращать пустой список если нет имен")
        void getListNames_WhenNoNames_ShouldReturnEmptyList() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

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
            TestCarService service = new TestCarService(true);

            // When & Then
            assertThrows(ServiceException.class, () -> service.getListNames());
        }
    }

    @Nested
    @DisplayName("Тесты метода getCarFilesList()")
    class GetCarFilesListTests {

        @Test
        @DisplayName("getCarFilesList() должен возвращать список файлов автомобилей")
        void getCarFilesList_ShouldReturnListOfFiles() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();
            CustomArrayList<String> expectedFiles = new CustomArrayList<>();
            expectedFiles.add("cars1.txt");
            expectedFiles.add("cars2.txt");
            service.setCarFilesList(expectedFiles);

            // When
            CustomArrayList<String> result = service.getCarFilesList();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("cars1.txt", result.get(0));
            assertEquals("cars2.txt", result.get(1));
        }

        @Test
        @DisplayName("getCarFilesList() должен возвращать пустой список если нет файлов")
        void getCarFilesList_WhenNoFiles_ShouldReturnEmptyList() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

            // When
            CustomArrayList<String> result = service.getCarFilesList();

            // Then
            assertNotNull(result);
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("getCarFilesList() должен выбрасывать ServiceException при ошибке")
        void getCarFilesList_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(true);

            // When & Then
            assertThrows(ServiceException.class, () -> service.getCarFilesList());
        }
    }

    @Nested
    @DisplayName("Тесты метода saveAll()")
    class SaveAllTests {

        @Test
        @DisplayName("saveAll() должен сохранять список автомобилей")
        void saveAll_ShouldSaveCarList() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);
            cars.add(CAR2);

            // When
            boolean result = service.saveAll(cars, "myCars");

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("saveAll() с null списком должен возвращать false")
        void saveAll_WithNullList_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

            // When
            boolean result = service.saveAll(null, "test");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveAll() с пустым именем должен возвращать false")
        void saveAll_WithEmptyName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

            // When
            boolean result = service.saveAll(cars, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveAll() должен выбрасывать ServiceException при ошибке")
        void saveAll_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(true);
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR1);

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
            TestCarService service = new TestCarService();

            // When
            boolean result = service.save(CAR1, "myGarage");

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("save() с null автомобилем должен возвращать false")
        void save_WithNullCar_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

            // When
            boolean result = service.save(null, "test");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() с пустым именем должен возвращать false")
        void save_WithEmptyName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

            // When
            boolean result = service.save(CAR1, "");

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() с null именем должен возвращать false")
        void save_WithNullName_ShouldReturnFalse() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();

            // When
            boolean result = service.save(CAR1, null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("save() должен выбрасывать ServiceException при ошибке")
        void save_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(true);

            // When & Then
            assertThrows(ServiceException.class, () -> service.save(CAR1, "test"));
        }
    }

    @Nested
    @DisplayName("Тесты метода saveCarListNames()")
    class SaveCarListNamesTests {

        @Test
        @DisplayName("saveCarListNames() должен сохранять список имен")
        void saveCarListNames_ShouldSaveNames() throws ServiceException {
            // Given
            TestCarService service = new TestCarService();
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
            TestCarService service = new TestCarService();
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
            TestCarService service = new TestCarService();

            // When
            boolean result = service.saveCarListNames(null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("saveCarListNames() должен выбрасывать ServiceException при ошибке")
        void saveCarListNames_WhenError_ShouldThrowException() {
            // Given
            TestCarService service = new TestCarService(true);
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
            TestCarService service = new TestCarService();

            // 1. Сохраняем автомобиль
            boolean saveResult = service.save(CAR1, "myCars");
            assertTrue(saveResult);

            // 2. Сохраняем список автомобилей
            CustomArrayList<Car> cars = new CustomArrayList<>();
            cars.add(CAR2);
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
            assertEquals(1, retrievedNames.size());

            // 5. Получаем список файлов
            CustomArrayList<String> files = service.getCarFilesList();
            assertNotNull(files);
        }
    }
}
