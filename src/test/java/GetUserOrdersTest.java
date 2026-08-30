import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserOrdersTest {

    private String accessToken;

    @AfterEach
    @Step("Удалить тестового пользователя")
    public void tearDown() {

        if (accessToken != null) {
            StellarBurgersApi.deleteUser(accessToken);
        }
    }

    @Test
    @Step("Получить заказы авторизованного пользователя")
    public void getOrdersWithAuthorizationTest() {

        createUser();

        Response response = StellarBurgersApi.getUserOrders(accessToken);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().get("orders"));
    }

    @Test
    @Step("Получить заказы без авторизации")
    public void getOrdersWithoutAuthorizationTest() {

        Response response =
                StellarBurgersApi.getUserOrdersWithoutAuthorization();

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));

        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }

    @Step("Создать пользователя")
    private void createUser() {

        String uniqueId = UUID.randomUUID().toString();

        User user = new User(
                "orders_" + uniqueId + "@mail.ru",
                "Password123",
                "OrdersUser"
        );

        Response response = StellarBurgersApi.createUser(user);

        assertEquals(200, response.statusCode());

        accessToken = response.jsonPath().getString("accessToken");
    }
}