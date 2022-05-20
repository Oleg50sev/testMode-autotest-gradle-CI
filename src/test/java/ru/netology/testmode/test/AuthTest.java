package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $(".button").click();
        $(byText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $("[name=login]").setValue(notRegisteredUser.getLogin());
        $("[name=password]").setValue(notRegisteredUser.getPassword());
        $(".button").click();
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[name=login]").setValue(blockedUser.getLogin());
        $("[name=password]").setValue(blockedUser.getPassword());
        $(".button").click();
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[name=login]").setValue(wrongLogin);
        $("[name=password]").setValue("registeredUser");
        $(".button").click();
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[name=login]").setValue("registeredUser");
        $("[name=password]").setValue(wrongPassword);
        $(".button").click();
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
