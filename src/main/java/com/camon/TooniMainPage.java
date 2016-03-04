package com.camon;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by jooyong on 2016-02-22.
 */
public class TooniMainPage {

    public SelenideElement getResult() {
        return $(".memberInfo");
    }
}
