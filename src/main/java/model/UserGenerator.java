package model;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    @Step("Generate random user")
    public static UserData getRandomUser(String emailParam, String passwordParam,
                                         String nameParam) {
        String email = emailParam + RandomStringUtils.randomAlphabetic(4) + "@gmail.com";
        String password = passwordParam + RandomStringUtils.randomAlphabetic(4);
        String name = nameParam + RandomStringUtils.randomAlphabetic(4);

        return new UserData(email, password, name);
    }

    @Step("Generate random user without email")
    public static UserData getRandomUserWithoutEmail(String emailParam, String passwordParam,
                                                     String nameParam){
        String email = emailParam;
        String password = passwordParam + RandomStringUtils.randomAlphabetic(4);
        String name = nameParam + RandomStringUtils.randomAlphabetic(4);

        return new UserData(email, password, name);
    }

    @Step("Generate random user without password")
    public static UserData getRandomUserWithoutPassword(String emailParam, String passwordParam,
                                                     String nameParam){
        String email = emailParam + RandomStringUtils.randomAlphabetic(4) + "@gmail.com";
        String password = passwordParam;
        String name = nameParam + RandomStringUtils.randomAlphabetic(4);

        return new UserData(email, password, name);
    }

    @Step("Generate random user without name")
    public static UserData getRandomUserWithoutName(String emailParam, String passwordParam,
                                                        String nameParam){
        String email = emailParam + RandomStringUtils.randomAlphabetic(4) + "@gmail.com";
        String password = passwordParam + RandomStringUtils.randomAlphabetic(4);
        String name = nameParam;

        return new UserData(email, password, name);
    }
}
