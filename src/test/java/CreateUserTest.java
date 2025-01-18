import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static model.UserGenerator.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    protected UserData userData;
    protected UserApi userApi;
    protected String token;

    @Before
    public void setUp() {
        userApi = new UserApi();
    }

    @After
    public void cleanUp() {
        if (token != null) {
            userApi.deleteUser(token)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Check that user is created")
    @Description("Check that unique user is created if all fields are passed")
    public void userCanBeCreatedTest() {
        userData = getRandomUser("Yulia", "password", "Yulia");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Check that user already exists")
    @Description("Check that user already exists when we use the same data and return error")
    public void createUserWithExistingDataTest() {
        userData = getRandomUser("Rinat", "password", "Rinat");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        ValidatableResponse responseSecond = userApi.createUser(userData);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User already exists"));
        token = responseFirst.extract().path("accessToken");
    }

    @Test
    @DisplayName("Cannot create user without email")
    @Description("Check that it is impossible to create a user without email and return error")

    public void createUserWithoutEmailTest() {
        userData = getRandomUserWithoutEmail("", "password", "Olga");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Cannot create user without password")
    @Description("Check that it is impossible to create a user without password and return error")

    public void createUserWithoutPasswordTest() {
        userData = getRandomUserWithoutPassword("Anna", "", "Anna");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Cannot create user without name")
    @Description("Check that it is impossible to create a user without name and return error")

    public void createUserWithoutNameTest() {
        userData = getRandomUserWithoutName("Anna", "password", "");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
        token = response.extract().path("accessToken");
    }
}
