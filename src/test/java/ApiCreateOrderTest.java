import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.RestAssured;
import model.Ingredients;
import model.UserAuthModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static TestData.TestData.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.stepsOrder.createOrderWithAuth;
import static steps.stepsOrder.createOrderWithoutAuth;
import static steps.stepsUser.*;

public class ApiCreateOrderTest{


    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        UserModel userCreate = new UserModel(EMAIL, PASSWORD, NAME);
        createUser(userCreate);
    }

    @Test
    @DisplayName("Код ответа 200 при создание заказа без авторизации пользователя")
    @Description("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest(){
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"));

        createOrderWithoutAuth(ingredients)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Код ответа 200 при создании заказа с авторизацией пользователя")
    @Description("Создание заказа с авторизацией")
    public void createOrderWithAuthTest() {
        UserAuthModel login = new UserAuthModel(EMAIL, PASSWORD);
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"));

        createOrderWithAuth(ingredients,getUserToken(login))
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Код ответа 400 при создании заказа без ингредиентов с авторизацией")
    @Description("Создание заказа без ингредиентов с авторизацией")
    public void createOrderWithoutIngredientsTest() {
        Ingredients ingredients = new Ingredients(Arrays.asList(null, null));
        UserAuthModel login = new UserAuthModel(EMAIL, PASSWORD);

        createOrderWithAuth(ingredients, getUserToken(login))
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("One or more ids provided are incorrect"));



    }

    @Test
    @DisplayName("Код ответа 500 при создании заказа с неверным хэшом ингредиентов и авторизации")
    @Description("Создание заказа с неверным хэшом ингредиентов и авторизацией пользователя")
    public void createOrderWithIncorrectHashIngredientsTest(){
        Ingredients ingredients = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6f54545", "61c0c5a71d55551f82001bdaaa70"));
        UserAuthModel login = new UserAuthModel(EMAIL, PASSWORD);

        createOrderWithAuth(ingredients, getUserToken(login))
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
