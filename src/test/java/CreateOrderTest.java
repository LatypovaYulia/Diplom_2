import api.OrderApi;
import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.OrderData;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static model.UserGenerator.getRandomUser;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    protected UserData userData;
    protected UserApi userApi;
    protected String token;
    protected OrderApi orderApi;
    protected OrderData orderData;

    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }

    @Test
    @DisplayName("Create order with authorization and ingredients")
    @Description("Check that order can be created with authorization and with ingredients")
    public void createOrderWithAuthorizationAndIngredientsTest() {
        userApi = new UserApi();
        userData = getRandomUser("Yulia", "password", "Yulia");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        token = response.extract().path("accessToken");

        List<String> ingredients = Arrays.asList(
                orderApi.getIngredientId(3),
                orderApi.getIngredientId(8),
                orderApi.getIngredientId(14)
        );

        orderData = new OrderData(ingredients);
        ValidatableResponse responseOrder = orderApi.createOrderWithAuthorizationAndIngredients(orderData, token);
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        if (token != null) {
            userApi.deleteUser(token)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Create order with authorization and without ingredients")
    @Description("Check that order cannot be created with authorization and without ingredients and return error")
    public void createOrderWithAuthorizationAndWithoutIngredientsTest() {
        userApi = new UserApi();
        userData = getRandomUser("Yulia", "password", "Yulia");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        token = response.extract().path("accessToken");

        orderData = new OrderData();
        ValidatableResponse responseOrder = orderApi.createOrderWithAuthorizationAndWithoutIngredients(token);
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));

        if (token != null) {
            userApi.deleteUser(token)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Create order without authorization and with ingredients")
    @Description("Check that order can be created without authorization and with ingredients")
    public void createOrderWithoutAuthorizationAndWithIngredientsTest() {

        List<String> ingredients = Arrays.asList(
                orderApi.getIngredientId(0),
                orderApi.getIngredientId(5),
                orderApi.getIngredientId(8),
                orderApi.getIngredientId(13)
        );

        orderData = new OrderData(ingredients);
        ValidatableResponse responseOrder = orderApi.createOrderWithoutAuthorizationAndWithIngredients(orderData);
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Create order without authorization and without ingredients")
    @Description("Check that order cannot be created without authorization and without ingredients and return error")
    public void createOrderWithoutAuthorizationAndWithoutIngredientsTest() {

        orderData = new OrderData();
        ValidatableResponse responseOrder = orderApi.createOrderWithoutAuthorizationAndWithoutIngredients();
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with invalid ingredient id")
    @Description("Check that order cannot be created with invalid ingredient id and return error")
    public void createOrderWithInvalidIngredientIdTest() {

        List<String> ingredients = Arrays.asList(
                orderApi.getIngredientId(2) + "0",
                orderApi.getIngredientId(7) + "0",
                orderApi.getIngredientId(3) + "0"
        );

        orderData = new OrderData(ingredients);
        ValidatableResponse responseOrder = orderApi.createOrderWithInvalidIngredientId(orderData);
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Get user orders with authorization")
    @Description("Check that we can get user orders with authorization")
    public void getUserOrdersWithAuthorizationTest() {
        userApi = new UserApi();
        userData = getRandomUser("Yulia", "password", "Yulia");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        token = response.extract().path("accessToken");

        orderData = new OrderData();
        ValidatableResponse responseOrder = orderApi.getUserOrdersWithAuthorization(token);
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        if (token != null) {
            userApi.deleteUser(token)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Get user orders without authorization")
    @Description("Check that we cannot get user orders without authorization and return error")
    public void getUserOrdersWithoutAuthorizationTest() {

        orderData = new OrderData();
        ValidatableResponse responseOrder = orderApi.getUserOrdersWithoutAuthorization();
        responseOrder.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
