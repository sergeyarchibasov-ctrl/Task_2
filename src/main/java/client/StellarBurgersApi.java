package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;

public class StellarBurgersApi {

    private static final String BASE_URL =
            "https://qa-stellarburgers.education-services.ru";

    @Step("Создать пользователя")
    public static Response createUser(User user) {

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                user.getEmail(),
                user.getPassword(),
                user.getName()
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/auth/register");
    }

    @Step("Создать пользователя без email")
    public static Response createUserWithoutEmail(User user) {

        String body = String.format(
                "{\"password\":\"%s\",\"name\":\"%s\"}",
                user.getPassword(),
                user.getName()
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/auth/register");
    }

    @Step("Создать пользователя без password")
    public static Response createUserWithoutPassword(User user) {

        String body = String.format(
                "{\"email\":\"%s\",\"name\":\"%s\"}",
                user.getEmail(),
                user.getName()
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/auth/register");
    }

    @Step("Создать пользователя без name")
    public static Response createUserWithoutName(User user) {

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}",
                user.getEmail(),
                user.getPassword()
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/auth/register");
    }

    @Step("Авторизоваться под пользователем")
    public static Response loginUser(User user) {

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}",
                user.getEmail(),
                user.getPassword()
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/auth/login");
    }

    @Step("Изменить данные пользователя")
    public static Response updateUser(
            String accessToken,
            String email,
            String password,
            String name
    ) {

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                email,
                password,
                name
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Изменить данные пользователя без авторизации")
    public static Response updateUserWithoutAuthorization(
            String email,
            String password,
            String name
    ) {

        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
                email,
                password,
                name
        );

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Создать заказ")
    public static Response createOrder(
            String accessToken,
            String ingredients
    ) {

        String body = "{\"ingredients\":" + ingredients + "}";

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post("/api/orders");
    }

    @Step("Создать заказ без авторизации")
    public static Response createOrderWithoutAuthorization(
            String ingredients
    ) {

        String body = "{\"ingredients\":" + ingredients + "}";

        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/api/orders");
    }

    @Step("Получить список ингредиентов")
    public static Response getIngredients() {

        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/ingredients");
    }

    @Step("Получить заказы пользователя")
    public static Response getUserOrders(String accessToken) {

        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    @Step("Получить заказы без авторизации")
    public static Response getUserOrdersWithoutAuthorization() {

        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/orders");
    }

    @Step("Удалить пользователя")
    public static Response deleteUser(String accessToken) {

        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}