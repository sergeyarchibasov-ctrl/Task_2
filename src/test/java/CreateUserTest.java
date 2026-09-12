import io.qameta.allure.Description;
import io.restassured.response.Response;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import steps.UserSteps;
import utils.UserGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @AfterEach
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @Description("Проверка успешного создания уникального пользователя")
    public void createUniqueUserTest() {
        User user = UserGenerator.getUniqueUser();

        Response response = userSteps.createUser(user);

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
    @Description("Проверка ошибки при создании уже зарегистрированного пользователя")
    public void createExistingUserTest() {
        User user = UserGenerator.getUniqueUser();

        Response firstResponse = userSteps.createUser(user);

        assertEquals(200, firstResponse.statusCode());

        accessToken =
                firstResponse.jsonPath().getString("accessToken");

        Response secondResponse =
                userSteps.createUser(user);

        assertEquals(403, secondResponse.statusCode());
        assertFalse(secondResponse.jsonPath().getBoolean("success"));
        assertEquals(
                "User already exists",
                secondResponse.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без обязательного поля")
    public void createUserWithoutRequiredFieldTest() {
        User user = UserGenerator.getUniqueUser();

        User userWithoutName = new User(
                user.getEmail(),
                user.getPassword(),
                null
        );

        Response response =
                userSteps.createUser(userWithoutName);

        assertEquals(403, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals(
                "Email, password and name are required fields",
                response.jsonPath().getString("message")
        );
    }
}