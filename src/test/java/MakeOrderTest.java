import client.CourierClient;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.runners.Parameterized;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.After;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

@RunWith(Parameterized.class)
public class MakeOrderTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    CourierClient courierClient = new CourierClient();
    private int track;

    public MakeOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {firstName}, {lastName}, {address}, {metroStation}, {phone}, {rentTime}, {deliveryDate}, {comment}, {color}") // добавили аннотацию
    public static Object[][] getOrder() {
        return new Object[][]{
                {"Andrey", "Kan", "Street 42", 1, "88005553535", 2, "2025-06-06", "Thx", new String[]{"BLACK"}},
                {"Andrey", "Kan", "Street 42", 1, "88005553535", 2, "2025-06-06", "Thx", new String[]{"BLACK", "GREY"}},
                {"Andrey", "Kan", "Street 42", 1, "88005553535", 2, "2025-06-06", "Thx", new String[]{}},

        };
    }

    @Test
    @DisplayName("Successful creation of an order with one, two and without colors")
    @Description("Заказ можно создать выбрав один или два цвета, либо вообще без цветов")
    public void runSuccessfulOrderOneColorTest(){
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ValidatableResponse response = courierClient.makeOrder(order);
        track = CourierClient.successfulOrder(response);
    }

    @After
    public void after(){
        ValidatableResponse response = courierClient.cancelOrder(track);
        CourierClient.successfulCancelOrder(response);
    }

}
