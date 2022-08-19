package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {
    @FindBy(id = "buttonSignUp")
    private WebElement signUpButton;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "inputUsername")
    private WebElement userNameField;

    @FindBy(id = "inputLastName")
    private WebElement lastNameField;

    @FindBy(id = "inputFirstName")
    private WebElement firstNameField;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public String getUsername(){
        return userNameField.getText();
    }

    public String getLastName(){
        return lastNameField.getText();
    }

    public String getFirstName(){
        return firstNameField.getText();
    }

    public String getPassword(){
        return passwordField.getText();
    }

    //test user sign up
    public void userSignUp(String firstName, String lastName, String username, String password){
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        userNameField.sendKeys(username);
        passwordField.sendKeys(password);
        signUpButton.click();
    }
}
