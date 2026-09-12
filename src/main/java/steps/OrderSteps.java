package steps;

import client.StellarBurgersApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderRequest;

import java.util.List;

public class OrderSteps {

    private final StellarBurgersApi api = new StellarBurgersApi();

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return api.getIngredients();
    }

    @Step("Получить два валидных хеша ингредиентов")
    public List<String> getValidIngredients() {
        Response response = api.getIngredients();

        List<String> ids = response.jsonPath()
                .getList("data._id", String.class);

        return ids.subList(0, 2);
    }

    @Step("Создать заказ авторизованного пользователя")
    public Response createOrder(
            String accessToken,
            List<String> ingredients
    ) {
        return api.createOrder(
                accessToken,
                new OrderRequest(ingredients)
        );
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuthorization(
            List<String> ingredients
    ) {
        return api.createOrderWithoutAuthorization(
                new OrderRequest(ingredients)
        );
    }

    @Step("Получить заказы авторизованного пользователя")
    public Response getUserOrders(String accessToken) {
        return api.getUserOrders(accessToken);
    }

    @Step("Получить заказы без авторизации")
    public Response getUserOrdersWithoutAuthorization() {
        return api.getUserOrdersWithoutAuthorization();
    }
}