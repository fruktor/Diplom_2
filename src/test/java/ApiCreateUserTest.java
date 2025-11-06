import io.qameta.allure.junit4.DisplayName;
import model.UserAuthModel;
import model.UserModel;
import org.junit.After;
import org.junit.Test;

import static TestData.TestData.*;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.stepsUser.*;

public class ApiCreateUserTest extends BaseApiTest{

    @Test
    @DisplayName("Код ответа 200 OK при создании уникального пользователя")
    public void createUniqueUser() {
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);

        createUser(user)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Код ответа 403 при создании существующего пользователя")
    public void createExistingUser() {
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        createUser(user);

        createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Код ответа 403, если при создании не указать Email")
    public void createUserWithoutEmail() {
        UserModel user = new UserModel(null, PASSWORD, NAME);

        createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Код ответа 403, если при создании не указать пароль")
    public void createUserWithoutPassword() {
        UserModel user = new UserModel(EMAIL, null, NAME);

        createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Код ответа 403, если при создании не указать имя")
    public void createUserWithoutName() {
        UserModel user = new UserModel(EMAIL, PASSWORD, null);

        createUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
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
