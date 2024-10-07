package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;


public class TC001_AccountRegisterationTest extends BaseClass {
    @Test(groups={"Regression", "Master"})
    public void verify_account_registration() {

        logger.info("***** Starting TC001_AccountRegisterationTest *****");
        try {
        HomePage hp = new HomePage(driver);
        hp.clickMyAccount();
        logger.info("Clicked on my account link");
        hp.clickRegister();
        logger.info("Clicked on register link");

        AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
        logger.info("Providing customer details...");
        regpage.setFirstName(randomString().toUpperCase());
        regpage.setLastName(randomString().toUpperCase());

        regpage.setEmail(randomString() + "@gmail.com"); // randomly generated email

        regpage.setTelephone(randomNumber());

        String password = randomAlphaNumeric();

        regpage.setPassword(password);
        regpage.setConfirmPassword(password);

        regpage.setPrivacyPolicy();
        regpage.clickContinue();

        logger.info("Validating expected message..");
        String confmsg = regpage.getConfirmationMsg();
        if(confmsg.equals("Your Account Has Been Created!"))
        {
            Assert.assertTrue(true);
        }
        else
        {
            logger.error("Test Failed...");
            logger.debug("Debug logs...");
            Assert.assertTrue(false);
        }
       // Assert.assertEquals(confmsg, "Your Account Has not Been Created!");
    }
        catch (Exception e)
        {
            Assert.fail();
        }
        logger.info("***** Finished TC001_AccountRegisterationTest *****");
}

}