import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeUserTest {

    private String accessToken;

    @AfterEach
    @Step("Удалить тестового пользователя")
    public void tearDown() {
        if (accessToken != null) {
            StellarBurgersApi.deleteUser(accessToken);
        }
    }

    @Test
    @Step("Изменить email с авторизацией")
    public void changeEmailWithAuthorizationTest() {

        User user = createUser();

        String newEmail = "new_" + UUID.randomUUID() + "@mail.ru";

        Response response = StellarBurgersApi.updateUser(
                accessToken,
                newEmail,
                user.getPassword(),
                user.getName()
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(
                newEmail,
                response.jsonPath().getString("user.email")
        );
    }

    @Test
    @Step("Изменить password с авторизацией")
    public void changePasswordWithAuthorizationTest() {

        User user = createUser();

        String newPassword = "NewPassword123";

        Response response = StellarBurgersApi.updateUser(
                accessToken,
                user.getEmail(),
                newPassword,
                user.getName()
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Изменить name с авторизацией")
    public void changeNameWithAuthorizationTest() {

        User user = createUser();

        String newName = "NewName";

        Response response = StellarBurgersApi.updateUser(
                accessToken,
                user.getEmail(),
                user.getPassword(),
                newName
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(
                newName,
                response.jsonPath().getString("user.name")
        );
    }

    @Test
    @Step("Изменить email без авторизации")
    public void changeEmailWithoutAuthorizationTest() {

        User user = getUniqueUser();

        String newEmail = "new_" + UUID.randomUUID() + "@mail.ru";

        Response response = StellarBurgersApi.updateUserWithoutAuthorization(
                newEmail,
                user.getPassword(),
                user.getName()
        );

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Step("Изменить password без авторизации")
    public void changePasswordWithoutAuthorizationTest() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.updateUserWithoutAuthorization(
                user.getEmail(),
                "NewPassword123",
                user.getName()
        );

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Step("Изменить name без авторизации")
    public void changeNameWithoutAuthorizationTest() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.updateUserWithoutAuthorization(
                user.getEmail(),
                user.getPassword(),
                "NewName"
        );

        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }

    @Step("Создать пользователя для изменения данных")
    private User createUser() {

        User user = getUniqueUser();

        Response response = StellarBurgersApi.createUser(user);

        assertEquals(200, response.statusCode());

        accessToken = response.jsonPath().getString("accessToken");

        return user;
    }

    @Step("Сгенерировать уникального пользователя")
    private User getUniqueUser() {

        String uniqueId = UUID.randomUUID().toString();

        return new User(
                "change_" + uniqueId + "@mail.ru",
                "Password123",
                "ChangeUser"
        );
    }
}