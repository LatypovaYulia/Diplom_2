package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.OrderData;

import static io.restassured.RestAssured.given;

public class OrderApi extends RestApi {
    public static final String GET_INGREDIENTS_DATA_URI = "/api/ingredients";
    public static final String CREATE_ORDER_URI = "/api/orders";

    @Step("Get ingredients data")
    public ValidatableResponse getIngredientsData() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(GET_INGREDIENTS_DATA_URI)
                .then();
    }

    @Step("Create order with authorization and ingredients")
    public ValidatableResponse createOrderWithAuthorizationAndIngredients(OrderData order, String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Create order with authorization and without ingredients")
    public ValidatableResponse createOrderWithAuthorizationAndWithoutIngredients(String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Create order without authorization and with ingredients")
    public ValidatableResponse createOrderWithoutAuthorizationAndWithIngredients(OrderData order) {
        return given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Create order without authorization and without ingredients")
    public ValidatableResponse createOrderWithoutAuthorizationAndWithoutIngredients() {
        return given()
                .spec(requestSpecification())
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Get ingredient ID")
    public String getIngredientId(int index) {
        return getIngredientsData().extract().path("data[" + index + "]._id");
    }

    @Step("Create order with invalid ingredient id")
    public ValidatableResponse createOrderWithInvalidIngredientId(OrderData order) {
        return given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(CREATE_ORDER_URI)
                .then();
    }

    @Step("Get user orders with authorization")
    public ValidatableResponse getUserOrdersWithAuthorization(String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .when()
                .get(CREATE_ORDER_URI)
                .then();
    }

    @Step("Get user orders without authorization")
    public ValidatableResponse getUserOrdersWithoutAuthorization() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(CREATE_ORDER_URI)
                .then();
    }
}
