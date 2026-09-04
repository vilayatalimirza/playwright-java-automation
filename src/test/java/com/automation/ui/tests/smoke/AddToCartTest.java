package com.automation.ui.tests.smoke;

import com.automation.core.BrowserFactory;
import com.automation.pages.CartPage;
import com.automation.pages.HomePage;
import com.automation.pages.ProductPage;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Smoke tests for AutomationExercise.com - Add to Cart functionality
 */
public class AddToCartTest {

    private static final Logger logger = LogManager.getLogger(AddToCartTest.class);
    private BrowserContext context;
    private Page page;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeClass
    public void setUp() {
        logger.info("Setting up Add to Cart smoke tests");
        context = BrowserFactory.getBrowserContext("chromium", true);
        page = context.newPage();
        homePage = new HomePage(page);
        productPage = new ProductPage(page);
        cartPage = new CartPage(page);
    }

    @Test(priority = 1)
    public void testAddProductToCart() {
        logger.info("Test: Add product to cart");
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded");
        
        homePage.clickFirstProduct();
        Assert.assertTrue(productPage.isProductDetailsDisplayed(), "Product details should be displayed");
        
        productPage.addToCart("1");
        logger.info("Product added to cart successfully");
    }

    @Test(priority = 2)
    public void testViewCart() {
        logger.info("Test: View cart");
        homePage.navigateToHomePage();
        homePage.clickViewCart();
        
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");
        Assert.assertTrue(cartPage.isCartNotEmpty(), "Cart should not be empty");
        logger.info("Cart items verified");
    }

    @Test(priority = 3)
    public void testCartItemDetails() {
        logger.info("Test: Verify cart item details");
        cartPage.navigateToCart();
        
        String productName = cartPage.getProductNameInCart(0);
        String productPrice = cartPage.getProductPriceInCart(0);
        
        Assert.assertNotNull(productName, "Product name should not be null");
        Assert.assertNotNull(productPrice, "Product price should not be null");
        logger.info("Cart item details verified - Name: {}, Price: {}", productName, productPrice);
    }

    @Test(priority = 4)
    public void testUpdateCartQuantity() {
        logger.info("Test: Update product quantity in cart");
        cartPage.navigateToCart();
        
        cartPage.updateProductQuantity(0, "2");
        String quantity = cartPage.getProductQuantityInCart(0);
        
        Assert.assertEquals(quantity.trim(), "2", "Quantity should be updated to 2");
        logger.info("Cart quantity updated successfully");
    }

    @Test(priority = 5)
    public void testRemoveProductFromCart() {
        logger.info("Test: Remove product from cart");
        cartPage.navigateToCart();
        int initialCount = cartPage.getCartItemsCount();
        
        cartPage.removeProductFromCart(0);
        int finalCount = cartPage.getCartItemsCount();
        
        Assert.assertTrue(finalCount < initialCount, "Item count should decrease after removal");
        logger.info("Product removed from cart successfully");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down Add to Cart smoke tests");
        if (context != null) {
            context.close();
        }
        BrowserFactory.closeBrowser();
    }
}
