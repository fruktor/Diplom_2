package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.UserAuthModel;
import model.UserModel;

import static constants.ApiConstant.*;
import static io.restassured.RestAssured.given;

public class stepsUser {


    @Step("Создание уникального пользователя")
    public static Response createUser(UserModel userModel) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(userModel)
                .when()
                .post(CREATE_USER_POST)
                .then()
                .extract().response();
    }

    @Step("Авторизация пользователя")
    public static Response authUser(UserAuthModel userAuthModel) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(userAuthModel)
                .when()
                .post(AUTH_USER_POST)
                .then()
                .extract().response();
    }

    @Step("Получение токена пользователя")
    public static String getUserToken(UserAuthModel userAuthModel) {
        Response response = authUser(userAuthModel);
        response
                .then().statusCode(200);
        return response.path("accessToken");
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return given().log().all()
                .header("Authorization",accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_USER)
                .then()
                .extract().response();
    }
}
