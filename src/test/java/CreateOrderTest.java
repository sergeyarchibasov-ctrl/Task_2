import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import steps.OrderSteps;
import steps.UserSteps;
import utils.UserGenerator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private String accessToken;

    @AfterEach
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @Description("Создание заказа авторизованным пользователем с ингредиентами")
    public void createOrderWithAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        List<String> ingredients =
                orderSteps.getValidIngredients();

        Response response =
                orderSteps.createOrder(
                        accessToken,
                        ingredients
                );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(
                response.jsonPath().get("order.number")
        );
    }

    @Test
    @Description("Создание заказа без авторизации с ингредиентами")
    public void createOrderWithoutAuthorizationTest() {
        List<String> ingredients =
                orderSteps.getValidIngredients();

        Response response =
                orderSteps.createOrderWithoutAuthorization(
                        ingredients
                );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(
                response.jsonPath().get("order.number")
        );
    }

    @Test
    @Description("Создание заказа без ингредиентов авторизованным пользователем")
    public void createOrderWithAuthorizationWithoutIngredientsTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        Response response =
                orderSteps.createOrder(
                        accessToken,
                        Collections.emptyList()
                );

        assertEquals(400, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "Ingredient ids must be provided",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Создание заказа без ингредиентов и без авторизации")
    public void createOrderWithoutAuthorizationWithoutIngredientsTest() {
        Response response =
                orderSteps.createOrderWithoutAuthorization(
                        Collections.emptyList()
                );

        assertEquals(400, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "Ingredient ids must be provided",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Создание заказа с неверным хешем ингредиента")
    public void createOrderWithInvalidIngredientHashTest() {
        List<String> ingredients =
                Collections.singletonList(
                        "invalid_ingredient_hash"
                );

        Response response =
                orderSteps.createOrderWithoutAuthorization(
                        ingredients
                );

        assertEquals(500, response.statusCode());
    }
}