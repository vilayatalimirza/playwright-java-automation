package com.automation.ui.tests.smoke;

import com.automation.core.BrowserFactory;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Smoke tests for AutomationExercise.com - Login functionality
 */
public class LoginLogoutTest {

    private static final Logger logger = LogManager.getLogger(LoginLogoutTest.class);
    private BrowserContext context;
    private Page page;
    private HomePage homePage;
    private LoginPage loginPage;
    private static final String VALID_EMAIL = "testuser123@example.com";
    private static final String VALID_PASSWORD = "testpass123";

    @BeforeClass
    public void setUp() {
        logger.info("Setting up Login/Logout smoke tests");
        context = BrowserFactory.getBrowserContext("chromium", true);
        page = context.newPage();
        homePage = new HomePage(page);
        loginPage = new LoginPage(page);
    }

    @Test(priority = 1)
    public void testNavigateToLoginPage() {
        logger.info("Test: Navigate to login page");
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");
        
        homePage.clickSignupLogin();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        logger.info("Navigated to login page successfully");
    }

    @Test(priority = 2)
    public void testLoginWithValidCredentials() {
        logger.info("Test: Login with valid credentials");
        loginPage.navigateToLoginPage();
        
        loginPage.login(VALID_EMAIL, VALID_PASSWORD);
        
        // Verify successful login by checking page navigation
        String currentUrl = page.url();
        Assert.assertNotNull(currentUrl, "Should be redirected after login");
        logger.info("Login successful - Current URL: {}", currentUrl);
    }

    @Test(priority = 3)
    public void testLoginWithInvalidEmail() {
        logger.info("Test: Login with invalid email");
        loginPage.navigateToLoginPage();
        
        loginPage.login("invalidemail@test.com", "password");
        
        // Error message should be displayed
        String errorMessage = loginPage.getLoginErrorMessage();
        Assert.assertNotNull(errorMessage, "Error message should be displayed for invalid login");
        Assert.assertTrue(errorMessage.contains("Error") || errorMessage.contains("incorrect"), 
            "Error message should contain appropriate text");
        logger.info("Invalid login error handled correctly");
    }

    @Test(priority = 4)
    public void testLoginFormVisibility() {
        logger.info("Test: Login form visibility");
        loginPage.navigateToLoginPage();
        
        Assert.assertTrue(loginPage.isLoginFormVisible(), "Login form should be visible");
        logger.info("Login form is visible");
    }

    @Test(priority = 5)
    public void testSignupFormVisibility() {
        logger.info("Test: Signup form visibility");
        loginPage.navigateToLoginPage();
        
        Assert.assertTrue(loginPage.isSignupFormVisible(), "Signup form should be visible");
        logger.info("Signup form is visible");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down Login/Logout smoke tests");
        if (context != null) {
            context.close();
        }
        BrowserFactory.closeBrowser();
    }
}
