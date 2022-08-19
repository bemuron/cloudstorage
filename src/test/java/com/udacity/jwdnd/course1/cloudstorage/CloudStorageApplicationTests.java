package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	//@Value("${server.port}")
	@LocalServerPort
	private int port;

	private WebDriver driver;
	private SignupPage signupPage;
	private LoginPage loginPage;

	private HomePage homePage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.signupPage = new SignupPage(this.driver);
		this.loginPage = new LoginPage(this.driver);
		this.homePage = new HomePage(this.driver);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("signup-msg")).getText().contains("You successfully signed up! Please sign in now."));

		driver.get("http://localhost:" + this.port + "/login");
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	//testing user signup
	@Test
	public void testUserSignUp(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String username = "tester7";
		String password = "password";
		String firstname = "Test6";
		String lastname = "User";

		driver.get("http://localhost:" + this.port + "/signup");

		signupPage.userSignUp(firstname, lastname, username, password);

		//String expectedUrl= driver.getCurrentUrl();

		webDriverWait.until(ExpectedConditions.titleContains("Login"));

		//driver.get("http://localhost:" + this.port + "/login");

	}

	@Test
	public void testUserLogin(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		String username = "testuser";
		String password = "password";

		driver.get("http://localhost:" + this.port + "/login");

		loginPage.userLogin(username, password);

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	@Test
	public void testHomePageNotAvailableWithoutLogin(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.titleContains("Login"));
	}

	@Test
	public void testSignUpLoginLogoutFlow(){
		//sign up a user
		doMockSignUp("New","User","NU","123");

		//login the user
		doLogIn("NU", "123");

		//check if user is at home page
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		//log out the user
		homePage.userLogout();

		//user must be at login page if they logged out
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

	}

	@Test
	public void testLoginCreateNoteVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		String noteTitle = "New note";
		String noteDesc = "note description";

		testUserLogin();

		//go to notes tab
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));

		//open the add note modal
		homePage.openAddNoteModal();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.id("noteModal")));

		//create note
		homePage.addNewNote(noteTitle, noteDesc);

		//go to notes tab again
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));

		//check for new note
		Assertions.assertTrue(driver.findElement(By.id("notesTable")).getText().contains("New note"));
	}

	@Test
	public void testLoginEditNoteSaveVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		String noteTitle = "edited note";
		String noteDesc = "edited description";

		testUserLogin();

		//go to notes tab
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));

		//click a note to edit
		homePage.editNoteButton();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.id("noteModal")));

		//edit note
		homePage.editNote(noteTitle, noteDesc);

		//go to notes tab again
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));

		//check for edited note
		Assertions.assertTrue(driver.findElement(By.id("notesTable")).getText().contains("edited note"));
	}

	@Test
	public void testLoginDeleteNoteVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		testUserLogin();

		//go to notes tab
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));

		//delete note
		homePage.deleteNoteButton();

		//go to notes tab again
		homePage.openNotesTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notesTable")));

		//check for edited note
		Assertions.assertFalse(driver.findElement(By.id("notesTable")).getText().contains("edited note"));
	}

	@Test
	public void testLoginCreateCredVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		String url = "some-url.com";
		String username = "username";
		String password = "password";

		testUserLogin();

		//go to credential tab
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));

		//open the add credential modal
		homePage.showAddCredentialModal();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.id("credentialModal")));

		//create credential
		homePage.saveCredential(url, username, password);

		//go to credential tab again
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		//check for new credential
		Assertions.assertTrue(driver.findElement(By.id("credentialTable")).getText().contains("some-url.com"));
	}

	@Test
	public void testLoginEditCredentialVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		String url = "main-url.com";
		String username = "username2";
		String password = "password2";

		testUserLogin();

		//go to credential tab
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));

		//click a credential to edit
		homePage.editCredentialButton();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
				By.id("credentialModal")));

		//edit credential
		homePage.saveCredential(url, username, password);

		//go to credential tab again
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		//check for edited credential
		Assertions.assertTrue(driver.findElement(By.id("credentialTable")).getText().contains("main-url.com"));
	}

	@Test
	public void testLoginDeleteCredentialVerifyChanges(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		testUserLogin();

		//go to credential tab
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialButton")));

		//delete credential
		homePage.deleteCredentialButton();

		//go to notes tab again
		homePage.openCredentialTab();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		//check for edited credential
		Assertions.assertFalse(driver.findElement(By.id("credentialTable")).getText().contains("main-url.com"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

}
