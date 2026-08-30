import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUserTest {

    private String accessToken;

    @AfterEach
    @Step("Удалить тестового пользователя")
    public void tearDown() {
        if (accessToken != null) {
            StellarBurgersApi.deleteUser(accessToken);
        }
    }

    @Test
    @Step("Войти под существующим пользователем")
    public void loginExistingUserTest() {

        User user = getUniqueUser();

        Response createResponse = StellarBurgersApi.createUser(user);

        assertEquals(200, createResponse.statusCode());

        accessToken = createResponse.jsonPath().getString("accessToken");

        Response loginResponse = StellarBurgersApi.loginUser(user);

        assertEquals(200, loginResponse.statusCode());
        assertTrue(loginResponse.jsonPath().getBoolean("success"));

        assertEquals(
                user.getEmail(),
                loginResponse.jsonPath().getString("user.email")
        );

        assertEquals(
                user.getName(),
                loginResponse.jsonPath().getString("user.name")
        );

        assertNotNull(loginResponse.jsonPath().getString("accessToken"));
        assertNotNull(loginResponse.jsonPath().getString("refreshToken"));
    }

    @Test
    @Step("Войти с неверным логином и паролем")
    public void loginWithIncorrectCredentialsTest() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.loginUser(user);

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));

        assertEquals(
                "email or password are incorrect",
                response.jsonPath().getString("message")
        );
    }

    @Step("Сгенерировать уникального пользователя")
    private User getUniqueUser() {

        String uniqueId = UUID.randomUUID().toString();

        return new User(
                "login_" + uniqueId + "@mail.ru",
                "Password123",
                "LoginUser"
        );
    }
}