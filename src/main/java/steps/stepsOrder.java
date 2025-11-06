package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Ingredients;

import java.util.List;

import static constants.ApiConstant.CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class stepsOrder {

    @Step("Создание заказа без авторизации пользователя")
    public static Response createOrderWithoutAuth(Ingredients ingredients) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER)
                .then()
                .extract().response();

    }

    @Step("Создание заказа с авторизацией пользователя")
    public static Response createOrderWithAuth(Ingredients ingredients, String accessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("accessToken", accessToken)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER)
                .then()
                .extract().response();
    }
}
