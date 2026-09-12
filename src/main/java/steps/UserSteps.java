package steps;

import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.LoginRequest;
import model.User;

public class UserSteps {

    private final StellarBurgersApi api = new StellarBurgersApi();

    @Step("Создать пользователя")
    public Response createUser(User user) {
        return api.createUser(user);
    }

    @Step("Создать пользователя и получить accessToken")
    public String createUserAndGetToken(User user) {
        Response response = api.createUser(user);
        return response.jsonPath().getString("accessToken");
    }

    @Step("Авторизоваться под пользователем")
    public Response loginUser(User user) {
        LoginRequest loginRequest =
                new LoginRequest(user.getEmail(), user.getPassword());

        return api.loginUser(loginRequest);
    }

    @Step("Авторизоваться с указанными данными")
    public Response loginUser(String email, String password) {
        return api.loginUser(new LoginRequest(email, password));
    }

    @Step("Изменить данные авторизованного пользователя")
    public Response updateUser(String accessToken, User user) {
        return api.updateUser(accessToken, user);
    }

    @Step("Изменить данные пользователя без авторизации")
    public Response updateUserWithoutAuthorization(User user) {
        return api.updateUserWithoutAuthorization(user);
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        if (accessToken != null) {
            api.deleteUser(accessToken);
        }
    }
}