package client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.LoginRequest;
import model.OrderRequest;
import model.User;

import static io.restassured.RestAssured.given;

public class StellarBurgersApi {

    private static final String BASE_URL =
            "https://qa-stellarburgers.education-services.ru";

    public Response createUser(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    public Response loginUser(LoginRequest loginRequest) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login");
    }

    public Response updateUser(String accessToken, User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    public Response updateUserWithoutAuthorization(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    public Response deleteUser(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }

    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/ingredients");
    }

    public Response createOrder(String accessToken, OrderRequest orderRequest) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body(orderRequest)
                .when()
                .post("/api/orders");
    }

    public Response createOrderWithoutAuthorization(OrderRequest orderRequest) {
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/api/orders");
    }

    public Response getUserOrders(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    public Response getUserOrdersWithoutAuthorization() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/orders");
    }
}