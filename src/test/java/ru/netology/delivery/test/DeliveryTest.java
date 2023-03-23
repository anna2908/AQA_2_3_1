package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownALl() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=\"city\"] input").setValue(validUser.getCity());
        $("[data-test-id=\"date\"] input").sendKeys(deleteString);
        $("[data-test-id=\"date\"] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(validUser.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//fieldset//span[@class='button__text']").click();
        $x("//div[@data-test-id=\"success-notification\"]//div[@class=\"notification__content\"]").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=\"success-notification\"]").should(visible);
        $("[data-test-id=\"date\"] input").sendKeys(deleteString);
        $("[data-test-id=\"date\"] input").setValue(secondMeetingDate);
        $x("//fieldset//span[@class='button__text']").click();
        $x("//div[@data-test-id=\"replan-notification\"]//div[@class=\"notification__content\"]").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=\"replan-notification\"]").should(visible);
        $x("//div[@data-test-id=\"replan-notification\"]//span[@class='button__text']").click();
        $x("//div[@data-test-id=\"success-notification\"]//div[@class=\"notification__content\"]").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
        $("[data-test-id=\"success-notification\"]").should(visible);
    }
}
