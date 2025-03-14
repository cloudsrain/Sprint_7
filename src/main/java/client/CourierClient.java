package client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.Credentials;
import model.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel";
    private static final String GET_ORDER_PATH = "/api/v1/orders";

    private final Gson gson = new Gson();

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then()
                .log().all();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginCourier(Credentials credentials) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then()
                .log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse makeOrder(Order order){
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then()
                .log().all();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrders() {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .when()
                .get(GET_ORDER_PATH)
                .then()
                .log().all();
    }


    @Step("Удаление курьера")
    public void deleteCourier(int courierId) {
        given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then()
                .log().all();
    }


    @Step("Отменить заказ")
    public ValidatableResponse cancelOrder(int track){
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .when()
                .put(CANCEL_ORDER_PATH + "?track=" + track)
                .then()
                .log().all();
    }

    @Step("Успешное создание учетной записи")
    public static void successfulCreation(ValidatableResponse response) {
        response.assertThat().statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Успешный логин")
    public static void successfulLogin (ValidatableResponse response) {
        response.assertThat().statusCode(200);
    }

    @Step("Успешное получение списка заказов")
    public static void successfulGetOrders(ValidatableResponse response) {
        response.assertThat().statusCode(200)
                .body("orders", notNullValue());
    }

    @Step("Получение id для дальнейшего удаления курьера")
    public static int extractCourierId(ValidatableResponse response) {
        response.assertThat().statusCode(200);
        return   response
                .extract()
                .jsonPath()
                .getInt("id");
    }

    @Step("Запрос с повторяющимся логином")
    public static void failedCreationDuplicateLogin(ValidatableResponse response) {
        response.assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Запрос без логина или пароля")
    public static void failedCreationMissingField(ValidatableResponse response) {
        response.assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Step("Запрос с несуществующей парой логин-пароль")
    public static void failedIncorrectLogin(ValidatableResponse response) {
        response.assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Запрос без логина или пароля")
    public static void failedLoginMissingField(ValidatableResponse response) {
        response.assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Успешное оформление заказа")
    public static int successfulOrder(ValidatableResponse response){
        response.assertThat().statusCode(201)
                .body("track", notNullValue());
        return response
                .extract()
                .jsonPath()
                .getInt("track");
    }

    @Step("Успешная отмена заказа")
    public static void successfulCancelOrder (ValidatableResponse response){
        response.assertThat().statusCode(200)
                .body("ok", equalTo(true));
    }

}
