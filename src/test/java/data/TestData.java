package TestData;

import com.github.javafaker.Faker;

public class TestData {
    private static final Faker faker = new Faker();
    public static final String URL = "https://stellarburgers.education-services.ru/";

    public static final String EMAIL = faker.internet().emailAddress();
    public static final String PASSWORD = faker.internet().password();
    public static final String NAME = faker.name().firstName();;

}
