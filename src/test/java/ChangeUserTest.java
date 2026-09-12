import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import steps.UserSteps;
import utils.UserGenerator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeUserTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @AfterEach
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @Description("Изменение email авторизованного пользователя")
    public void changeEmailWithAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        String newEmail =
                "new_" + UUID.randomUUID() + "@mail.ru";

        User changedUser = new User(
                newEmail,
                user.getPassword(),
                user.getName()
        );

        Response response =
                userSteps.updateUser(accessToken, changedUser);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(
                newEmail,
                response.jsonPath().getString("user.email")
        );
    }

    @Test
    @Description("Изменение password авторизованного пользователя")
    public void changePasswordWithAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        User changedUser = new User(
                user.getEmail(),
                "NewPassword123",
                user.getName()
        );

        Response response =
                userSteps.updateUser(accessToken, changedUser);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Description("Изменение name авторизованного пользователя")
    public void changeNameWithAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        User changedUser = new User(
                user.getEmail(),
                user.getPassword(),
                "NewName"
        );

        Response response =
                userSteps.updateUser(accessToken, changedUser);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
        assertEquals(
                "NewName",
                response.jsonPath().getString("user.name")
        );
    }

    @Test
    @Description("Попытка изменения email пользователя без авторизации")
    public void changeEmailWithoutAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        User changedUser = new User(
                "new_" + UUID.randomUUID() + "@mail.ru",
                user.getPassword(),
                user.getName()
        );

        Response response =
                userSteps.updateUserWithoutAuthorization(changedUser);

        checkUnauthorizedResponse(response);
    }

    @Test
    @Description("Попытка изменения password пользователя без авторизации")
    public void changePasswordWithoutAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        User changedUser = new User(
                user.getEmail(),
                "NewPassword123",
                user.getName()
        );

        Response response =
                userSteps.updateUserWithoutAuthorization(changedUser);

        checkUnauthorizedResponse(response);
    }

    @Test
    @Description("Попытка изменения name пользователя без авторизации")
    public void changeNameWithoutAuthorizationTest() {
        User user = UserGenerator.getUniqueUser();

        accessToken =
                userSteps.createUserAndGetToken(user);

        User changedUser = new User(
                user.getEmail(),
                user.getPassword(),
                "NewName"
        );

        Response response =
                userSteps.updateUserWithoutAuthorization(changedUser);

        checkUnauthorizedResponse(response);
    }

    private void checkUnauthorizedResponse(Response response) {
        assertEquals(401, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "You should be authorised",
                response.jsonPath().getString("message")
        );
    }
}