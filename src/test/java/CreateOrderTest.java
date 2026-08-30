import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderTest {

    private String accessToken;

    @AfterEach
    @Step("Удалить тестового пользователя")
    public void tearDown() {

        if (accessToken != null) {
            StellarBurgersApi.deleteUser(accessToken);
        }
    }

    @Test
    @Step("Создать заказ с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationTest() {

        createUser();

        String ingredients = getValidIngredients();

        Response response = StellarBurgersApi.createOrder(
                accessToken,
                ingredients
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().getString("order.number"));
    }

    @Test
    @Step("Создать заказ без авторизации с ингредиентами")
    public void createOrderWithoutAuthorizationTest() {

        String ingredients = getValidIngredients();

        Response response = StellarBurgersApi.createOrderWithoutAuthorization(
                ingredients
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().getString("order.number"));
    }

    @Test
    @Step("Создать заказ с авторизацией без ингредиентов")
    public void createOrderWithAuthorizationWithoutIngredientsTest() {

        createUser();

        Response response = StellarBurgersApi.createOrder(
                accessToken,
                "[]"
        );

        assertEquals(400, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "Ingredient ids must be provided",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Step("Создать заказ без авторизации и без ингредиентов")
    public void createOrderWithoutAuthorizationWithoutIngredientsTest() {

        Response response = StellarBurgersApi.createOrderWithoutAuthorization(
                "[]"
        );

        assertEquals(400, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "Ingredient ids must be provided",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Step("Создать заказ с неверным хешем ингредиента")
    public void createOrderWithInvalidIngredientHashTest() {

        Response response = StellarBurgersApi.createOrderWithoutAuthorization(
                "[\"invalid_ingredient_hash\"]"
        );

        assertEquals(500, response.statusCode());
    }

    @Step("Создать пользователя")
    private void createUser() {

        String uniqueId = UUID.randomUUID().toString();

        User user = new User(
                "order_" + uniqueId + "@mail.ru",
                "Password123",
                "OrderUser"
        );

        Response response = StellarBurgersApi.createUser(user);

        assertEquals(200, response.statusCode());

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Step("Получить ID существующих ингредиентов")
    private String getValidIngredients() {

        Response response = StellarBurgersApi.getIngredients();

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));

        List<String> ids = response.jsonPath().getList(
                "data._id",
                String.class
        );

        assertFalse(ids.isEmpty());

        return "[\"" + ids.get(0) + "\",\"" + ids.get(1) + "\"]";
    }
}