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

public class ChangeDataTest {
    protected UserData userData;
    protected UserData userDataNew;
    protected UserApi userApi;
    protected String token;
    protected String email;
    protected String name;
    protected String password;

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
    @DisplayName("Change user email with authorization")
    @Description("Check that user email can be successfully updated with authorization")
    public void changeUserEmailWithAuthorizationTest() {

        userData = getRandomUser("Roman", "password", "Roman");
        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData("E" + email, password, name);

        ValidatableResponse responseSecond = userApi.changeUserWithAuthorization(userDataNew, token);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Change user email to existing email with authorization ")
    @Description("Check that user email cannot be changed to existing email with authorization and return error")
    public void changeUserEmailToExistingEmailWithAuthorizationTest() {

        UserData userData2;
        String token2;
        String email2;

        userData = getRandomUser("Ivan", "password", "Ivan");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        password = userData.getPassword();
        name = userData.getName();

        userData2 = getRandomUser("Roman", "password", "Roman");

        ValidatableResponse responseSecond = userApi.createUser(userData2);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token2 = responseSecond.extract().path("accessToken");
        email2 = userData2.getEmail();

        userDataNew = new UserData(email2, password, name);

        ValidatableResponse responseThird = userApi.changeUserWithAuthorization(userDataNew, token);
        responseThird.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User with such email already exists"));

        if (token2 != null) {
            userApi.deleteUser(token2)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .body("success", is(true));
        }
    }

    @Test
    @DisplayName("Change user password with authorization")
    @Description("Check that user password can be successfully updated with authorization")
    public void changeUserPasswordWithAuthorizationTest() {

        userData = getRandomUser("Alina", "password", "Alina");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData(email, password + "8", name);

        ValidatableResponse responseSecond = userApi.changeUserWithAuthorization(userDataNew, token);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Change user name with authorization")
    @Description("Check that user name can be successfully updated with authorization")
    public void changeUserNameWithAuthorizationTest() {

        userData = getRandomUser("Artur", "password", "Artur");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData(email, password, name + "5");

        ValidatableResponse responseSecond = userApi.changeUserWithAuthorization(userDataNew, token);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Change user email without authorization")
    @Description("Check that user email cannot be changed without authorization and return error")
    public void changeUserEmailWithoutAuthorizationReturnErrorTest() {

        userData = getRandomUser("Dan", "password", "Dan");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData("Q" + email, password, name);

        ValidatableResponse responseSecond = userApi.changeUserWithoutAuthorization(userDataNew);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Change user password without authorization")
    @Description("Check that user password cannot be changed without authorization and return error")
    public void changeUserPasswordWithoutAuthorizationReturnErrorTest() {

        userData = getRandomUser("Harry", "password", "Harry");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData(email, password + "0", name);

        ValidatableResponse responseSecond = userApi.changeUserWithoutAuthorization(userDataNew);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Change user name without authorization")
    @Description("Check that user name cannot be changed without authorization and return error")
    public void changeUserNameWithoutAuthorizationReturnErrorTest() {

        userData = getRandomUser("Ron", "password", "Ron");

        ValidatableResponse responseFirst = userApi.createUser(userData);
        responseFirst.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));

        token = responseFirst.extract().path("accessToken");
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();

        userDataNew = new UserData(email, password, name + "7");

        ValidatableResponse responseSecond = userApi.changeUserWithoutAuthorization(userDataNew);
        responseSecond.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
