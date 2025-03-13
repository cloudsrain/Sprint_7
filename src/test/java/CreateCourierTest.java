import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Test;

public class CreateCourierTest {

    private Courier courier;
    private final CourierClient courierClient = new CourierClient();
    private boolean courierCreated = false;


    @Test
    @DisplayName("Successful courier creation")
    @Description("Курьер создан, сервер вернул код 201")
    public void runCreateCourierTest() {
        courier = new Courier("ado", "1212", "test");

        ValidatableResponse response = courierClient.createCourier(courier);
        CourierClient.successfulCreation(response);
        courierCreated = true;

    }

    @Test
    @DisplayName("Error when creating a courier with an already taken login")
    @Description("Нельзя зарегистрировать курьера с логином, который уже существует")
    public void runCreateCourierWithExistingLoginTest() {
        courier = new Courier("ado", "1212", "test");

        courierClient.createCourier(courier);
        ValidatableResponse secondResponse = courierClient.createCourier(courier);

        courierCreated = true;

        CourierClient.failedCreationDuplicateLogin(secondResponse);
    }

    @Test
    @DisplayName("Error when creating a courier without a login")
    @Description("Запрос отклонён, так как логин отсутствует")
    public void runCreateCourierWithoutLoginTest() {
        courier = new Courier("", "1212", "test");

        ValidatableResponse response = courierClient.createCourier(courier);
        CourierClient.failedCreationMissingField(response);
    }

    @Test
    @DisplayName("Error when creating a courier without a password")
    @Description("Запрос отклонён, так как пароль отсутствует")
    public void runCreateCourierWithoutPasswordTest() {
        courier = new Courier("ado", "", "test");

        ValidatableResponse response = courierClient.createCourier(courier);
        CourierClient.failedCreationMissingField(response);
    }

    @After
    public void after() {
        Credentials credentials = Credentials.fromCourier(courier);
        if (courierCreated){
            // -1 означает, что курьер не создан
            int courierId = CourierClient.extractCourierId(courierClient.loginCourier(credentials));
            if (courierId > 0) { // Если ID получен, значит курьер создан
                courierClient.deleteCourier(courierId);
            }
        }
    }
}
