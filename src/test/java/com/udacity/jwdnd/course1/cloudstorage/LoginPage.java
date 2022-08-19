package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "inputUsername")
    private WebElement userNameField;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void userLogin(String username, String password){
        userNameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
