package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.UserData;

import static io.restassured.RestAssured.given;

public class UserApi extends RestApi {

    public static final String CREATE_USER_URI = "/api/auth/register";
    public static final String DELETE_USER_URI = "/api/auth/user";
    public static final String LOGIN_USER_URI = "/api/auth/login";
    public static final String CHANGE_USER_URI = "/api/auth/user";

    @Step("Create user")
    public ValidatableResponse createUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_URI)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .when()
                .delete(DELETE_USER_URI)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse loginUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_URI)
                .then();
    }

    @Step("Change user with authorization")
    public ValidatableResponse changeUserWithAuthorization(UserData user, String token) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .and()
                .header("Authorization", token)
                .when()
                .patch(CHANGE_USER_URI)
                .then();
    }
    @Step("Change user without authorization")
    public ValidatableResponse changeUserWithoutAuthorization(UserData user) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .patch(CHANGE_USER_URI)
                .then();
    }


}
