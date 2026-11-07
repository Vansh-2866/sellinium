
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;

public class logintest {

    WebDriver driver;

    // Runs before every test case
    @BeforeMethod
    public void setup() {

    	ChromeOptions options = new ChromeOptions();

    	if ("true".equalsIgnoreCase(System.getenv("SE_HEADLESS"))) {
    	    options.addArguments("--headless");
    	    options.addArguments("--no-sandbox");
    	    options.addArguments("--disable-dev-shm-usage");
    	}

    	driver = new ChromeDriver(options);

        File file = new File("webpage/login.html");

        driver.get(file.toURI().toString());

        System.out.println("Login page opened");
    }

    // Test Case 1: Verify page title
    @Test
    public void verifyPageTitle() {

        String title = driver.getTitle();

        Assert.assertEquals(title, "Local Login Page");

        System.out.println("Page title verified");
    }

    // Test Case 2: Verify login fields
    @Test
    public void verifyLoginFields() {

        Assert.assertTrue(
            driver.findElement(By.id("username")).isDisplayed());

        Assert.assertTrue(
            driver.findElement(By.id("password")).isDisplayed());

        Assert.assertTrue(
            driver.findElement(By.id("loginBtn")).isDisplayed());

        System.out.println("Login fields verified");
    }

    // Test Case 3: Verify successful login
    @Test
    public void verifySuccessfulLogin() {

        driver.findElement(By.id("username"))
                .sendKeys("admin");

        driver.findElement(By.id("password"))
                .sendKeys("12345");

        driver.findElement(By.id("loginBtn"))
                .click();

        String message = driver.findElement(
                By.id("message")).getText();

        Assert.assertEquals(message, "Login Successful!");

        System.out.println("Successful login verified");
    }

    // Test Case 4: Verify invalid login
    @Test
    public void verifyInvalidLogin() {

        driver.findElement(By.id("username"))
                .sendKeys("wronguser");

        driver.findElement(By.id("password"))
                .sendKeys("wrongpass");

        driver.findElement(By.id("loginBtn"))
                .click();

        String message = driver.findElement(
                By.id("message")).getText();

        Assert.assertEquals(
            message,
            "Invalid Username or Password!");

        System.out.println("Invalid login verified");
    }

    // Test Case 5: Verify empty form validation
    @Test
    public void verifyEmptyLogin() {

        driver.findElement(By.id("loginBtn")).click();

        Boolean isValid = (Boolean)
            ((JavascriptExecutor) driver)
            .executeScript(
                "return document.getElementById('loginForm').checkValidity();");

        Assert.assertFalse(isValid);

        System.out.println("Empty form validation verified");
    }

    // Runs after every test case
    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
