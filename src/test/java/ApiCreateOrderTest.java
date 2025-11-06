import io.qameta.allure.internal.shadowed.jackson.databind.ser.Serializers;
import io.qameta.allure.junit4.DisplayName;

import model.Ingredients;
import model.UserAuthModel;
import model.UserModel;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static TestData.TestData.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.stepsOrder.createOrderWithAuth;
import static steps.stepsOrder.createOrderWithoutAuth;
import static steps.stepsUser.*;

public class ApiCreateOrderTest extends BaseApiTest {


    @Test
    @DisplayName("Код ответа 200 при создание заказа без авторизации пользователя")
    public void createOrderWithoutAuthTest(){
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"));

        createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Код ответа 200 при создании заказа с авторизацией пользователя")
    public void createOrderWithAuthTest() {
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        UserAuthModel login = new UserAuthModel(EMAIL, PASSWORD);
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"));

        createUser(user);

        createOrderWithAuth(ingredients,getUserToken(login))
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Код ответа 400 при создании заказа без ингредиентов и без авторизации")
    public void createOrderWithoutIngredientsTest() {
        Ingredients ingredients = new Ingredients(Arrays.asList(null, null));

        createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Код ответа 500 при создании заказа с неверным хэшом ингредиентов и без авторизации")
    public void createOrderWithIncorrectHashIngredientsTest(){
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f54545", "61c0c5a71d55551f82001bdaaa70"));

        createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
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
