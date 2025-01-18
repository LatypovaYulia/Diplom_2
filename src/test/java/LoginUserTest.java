import api.UserApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static model.UserGenerator.getRandomUser;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    protected UserData userData;
    protected UserData userData2;
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
    @DisplayName("Check that user can log in")
    @Description("Check that user can log in with existing user")
    public void userCanLogInTest() {
        userData = getRandomUser("Alla", "password", "Alla");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        ValidatableResponse loginResponse = userApi.loginUser(userData);
        loginResponse.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("User cannot log in with incorrect email и password")
    @Description("System will return error if enter incorrect email и password")
    public void incorrectUserDataReturnErrorTest() {
        userData = getRandomUser("Peter", "password", "Peter");

        ValidatableResponse response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = response.extract().path("accessToken");

        userData2 = getRandomUser("Bob", "password", "Bob");
        ValidatableResponse loginResponse = userApi.loginUser(userData2);
        loginResponse.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}
