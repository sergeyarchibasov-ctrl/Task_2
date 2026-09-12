import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import steps.UserSteps;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUserTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @AfterEach
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @Description("Проверка успешной авторизации существующего пользователя")
    public void loginExistingUserTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        Response response =
                userSteps.loginUser(user);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(
                user.getEmail(),
                response.jsonPath().getString("user.email")
        );
        assertEquals(
                user.getName(),
                response.jsonPath().getString("user.name")
        );
        assertNotNull(
                response.jsonPath().getString("accessToken")
        );
        assertNotNull(
                response.jsonPath().getString("refreshToken")
        );
    }

    @Test
    @Description("Проверка ошибки авторизации с неверными логином и паролем")
    public void loginWithIncorrectCredentialsTest() {
        Response response = userSteps.loginUser(
                "wrong_" + System.currentTimeMillis() + "@mail.ru",
                "WrongPassword"
        );

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "email or password are incorrect",
                response.jsonPath().getString("message")
        );
    }
}