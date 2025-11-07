import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.UserAuthModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static TestData.TestData.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.stepsUser.*;
import static steps.stepsUser.getUserToken;

public class ApiLoginUserTest{

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        UserModel userCreate = new UserModel(EMAIL, PASSWORD, NAME);
        createUser(userCreate);
    }

    @Test
    @DisplayName("Код 200 при входе с существующими данными")
    @Description("Авторизация существующими данными")
    public void userLogin() {
        UserAuthModel user = new UserAuthModel(EMAIL, PASSWORD);

        authUser(user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Ошибка 401 при авторизации без Email")
    @Description("Авторизация без Email")
    public void userLoginWithoutEmail() {
        UserAuthModel user = new UserAuthModel(null, PASSWORD);

        authUser(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Ошибка 401 при авторизации без пароля")
    @Description("Авторизация без пароля")
    public void userLoginWithoutPassword() {
        UserAuthModel user = new UserAuthModel(EMAIL, null);

        authUser(user)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        UserAuthModel login = new UserAuthModel(EMAIL, PASSWORD);

        try {
            deleteUser(getUserToken(login));
        } catch (AssertionError | NullPointerException e) {

        }
    }
}
