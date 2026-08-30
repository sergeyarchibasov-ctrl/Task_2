import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTest {

    private String accessToken;

    @AfterEach
    @Step("Удалить тестового пользователя")
    public void tearDown() {
        if (accessToken != null) {
            StellarBurgersApi.deleteUser(accessToken);
        }
    }

    @Test
    @Step("Создать уникального пользователя")
    public void createUniqueUserTest() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.createUser(user);

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

        assertNotNull(response.jsonPath().getString("accessToken"));
        assertNotNull(response.jsonPath().getString("refreshToken"));

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @Step("Создать уже зарегистрированного пользователя")
    public void createExistingUserTest() {

        User user = getUniqueUser();

        Response firstResponse = StellarBurgersApi.createUser(user);

        assertEquals(200, firstResponse.statusCode());
        assertTrue(firstResponse.jsonPath().getBoolean("success"));

        accessToken = firstResponse.jsonPath().getString("accessToken");

        Response secondResponse = StellarBurgersApi.createUser(user);

        assertEquals(403, secondResponse.statusCode());
        assertFalse(secondResponse.jsonPath().getBoolean("success"));

        assertEquals(
                "User already exists",
                secondResponse.jsonPath().getString("message")
        );
    }

    @Test
    @Step("Создать пользователя без обязательного поля")
    public void createUserWithoutRequiredFieldTest() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.createUserWithoutName(user);

        assertEquals(403, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));

        assertEquals(
                "Email, password and name are required fields",
                response.jsonPath().getString("message")
        );
    }

    @Step("Сгенерировать уникального пользователя")
    private User getUniqueUser() {

        String uniqueId = UUID.randomUUID().toString();

        return new User(
                "test_" + uniqueId + "@mail.ru",
                "Password123",
                "TestUser"
        );
    }
}