import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import steps.OrderSteps;
import steps.UserSteps;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserOrdersTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private String accessToken;

    @AfterEach
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @Description("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        Response response =
                orderSteps.getUserOrders(accessToken);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertNotNull(response.jsonPath().get("orders"));
    }

    @Test
    @Description("Попытка получения заказов без авторизации")
    public void getOrdersWithoutAuthorizationTest() {
        Response response =
                orderSteps.getUserOrdersWithoutAuthorization();

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }
}