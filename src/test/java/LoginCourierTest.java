import client.CourierClient;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class LoginCourierTest {

    private Courier courier;
    private final CourierClient courierClient = new CourierClient();
    private int courierId = -1; // -1 означает, что курьер не создан
    private boolean courierCreated;

    @Before
    public void setUp() {
        courier = new Courier("ado", "1212", "test");
        Credentials credentials = Credentials.fromCourier(courier);

        ValidatableResponse response = courierClient.createCourier(courier);
        CourierClient.successfulCreation(response);
        courierCreated = true;
        courierId = CourierClient.extractCourierId(courierClient.loginCourier(credentials));
    }

    @Test
    @DisplayName("Successful courier login")
    @Description("Курьер может залогиниться с валидными данными, успешный заврос возращает id")
    public void runSuccessfulCourierLoginTest(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = courierClient.loginCourier(credentials);
        CourierClient.successfulLogin(response);
    }

    @Test
    @DisplayName("Error on invalid login")
    @Description("Система возвращает ошибку, если указаны неверный логин")
    public void runErrorOnInvalidLoginTest() {
        ValidatableResponse response = courierClient.loginCourier(new Credentials("ad0", "1212"));
        CourierClient.failedIncorrectLogin(response);
    }

    @Test
    @DisplayName("Error on invalid password")
    @Description("Система возвращает ошибку, если указаны неверный пароль")
    public void runErrorOnInvalidPasswordTest() {
        ValidatableResponse response = courierClient.loginCourier(new Credentials("ado", "1213"));
        CourierClient.failedIncorrectLogin(response);
    }

    @Test
    @DisplayName("Error when a login field is missing")
    @Description("Если в запросе отсутствует логин, система возвращает ошибку")
    public void runErrorOnMissingLoginFieldTest() {
        ValidatableResponse response = courierClient.loginCourier(new Credentials("", "1212"));
        CourierClient.failedLoginMissingField(response);
    }

    @Test
    @DisplayName("Error when a password field is missing")
    @Description("Если в запросе отсутствует пароль, система возвращает ошибку")
    public void runErrorOnMissingPasswordFieldTest() {
        ValidatableResponse response = courierClient.loginCourier(new Credentials("ado", ""));
        CourierClient.failedLoginMissingField(response);
    }

    @After
    public void after() {
        if (courierCreated){
                courierClient.deleteCourier(courierId);
            }
        }
    }


