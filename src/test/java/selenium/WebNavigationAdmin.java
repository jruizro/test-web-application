package selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class WebNavigationAdmin {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before public void setUp() throws Exception {

        driver = new HtmlUnitDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test public void testWebNavigationAdmin() throws Exception {
        driver.get(baseUrl + "/");
        driver.findElement(By.id("username_input")).clear();
        driver.findElement(By.id("username_input")).sendKeys("admin");
        driver.findElement(By.id("password_input")).clear();
        driver.findElement(By.id("password_input")).sendKeys("admin");
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.cssSelector("li")).click();
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.xpath("//a[2]/li")).click();
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.xpath("//a[3]/li")).click();
        driver.findElement(By.xpath("//input[@value='Log out!']")).click();
    }

    @After public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
