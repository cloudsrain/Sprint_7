import client.CourierClient;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;


public class OrderListTest {
    CourierClient courierClient = new CourierClient();

    @Test
    @DisplayName("Get order list")
    @Description("Проверка, что в ответ приходит список заказов")
    public void runGetOrderTest(){
        ValidatableResponse response = courierClient.getOrders();
        CourierClient.successfulGetOrders(response);
    }
}
