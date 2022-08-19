package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logoutBtn")
    private WebElement logOutButton;

    @FindBy(id = "note-id")
    private WebElement noteIdField;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteBtn;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTabBtn;

    @FindBy(id = "editNoteButton")
    private WebElement editNoteBtn;

    @FindBy(id = "deleteNoteButton")
    private WebElement deleteNoteBtn;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescField;

    @FindBy(id = "noteSubmitBtn")
    private WebElement submitNoteButton;

    //*********
    @FindBy(id = "addCredentialButton")
    private WebElement addCredBtn;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credsTabBtn;

    @FindBy(id = "editCredButton")
    private WebElement editCredBtn;

    @FindBy(id = "deleteCredButton")
    private WebElement deleteCredBtn;

    @FindBy(id = "credential-url")
    private WebElement credUrlField;

    @FindBy(id = "credential-username")
    private WebElement credUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credPasswordField;

    @FindBy(id = "credSubmitBtn")
    private WebElement submitCredButton;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void userLogout(){
        logOutButton.click();
    }

    //note tests methods
    public int getNoteId(){
        return Integer.parseInt(noteIdField.getAttribute("textContent"));
    }

    public String getNoteTitle(){
        return noteTitleField.getText();
    }

    public String getNoteDescription(){
        return noteDescField.getText();
    }

    public void editNote(String noteTitle, String noteDescription){
        noteTitleField.sendKeys(noteTitle);
        noteDescField.sendKeys(noteDescription);
        submitNoteButton.click();
    }

    public void deleteNoteButton(){
        deleteNoteBtn.click();
    }

    public void editNoteButton(){
        editNoteBtn.click();
    }

    public void openAddNoteModal(){
        addNoteBtn.click();
    }

    public void openNotesTab(){
        notesTabBtn.click();
    }

    public void addNewNote(String noteTitle, String noteDescription){

        noteTitleField.sendKeys(noteTitle);
        noteDescField.sendKeys(noteDescription);
        submitNoteButton.click();
    }

    public void saveCredential(String url, String username, String password){
        credUrlField.sendKeys(url);
        credUsernameField.sendKeys(username);
        credPasswordField.sendKeys(password);
        submitCredButton.click();
    }

    public void deleteCredentialButton(){
        deleteCredBtn.click();
    }

    public void editCredentialButton(){
        editCredBtn.click();
    }

    public void showAddCredentialModal(){
        addCredBtn.click();
    }

    public void openCredentialTab(){
        credsTabBtn.click();
    }
}
