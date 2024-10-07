package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Dashboard extends BasePage{

    public Dashboard(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(xpath = "//h6[normalize-space()='Dashboard']")
    WebElement DashboardText;

    public String getDashboardText() {
            return DashboardText.getText();
    }
}
