package testBase;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;


//this base class is the parent class of all the test classes.
public class BaseClass {
    public  static WebDriver driver;
    public Logger logger;
    public Properties p;
    @BeforeClass(groups = {"Sanity", "Regression", "Master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {
        //Loading config.properties file
        FileReader file = new FileReader("./src//test//resources//config.properties");
        p= new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());   //LOG4J2

        if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();

          //OS
          if(os.equalsIgnoreCase("windows"))
          {
              capabilities.setPlatform(Platform.WIN11);
          }
          else if(os.equalsIgnoreCase("linux"))
          {
                capabilities.setPlatform(Platform.LINUX);
          }
          else
          {
              System.out.println("No matching os");
              return;
          }

          //BROWSER
            switch (br.toLowerCase())
            {
                case "chrome" : capabilities.setBrowserName("chrome"); break;
                case "edge" : capabilities.setBrowserName("MicrosoftEdge"); break;
                case "firefox" : capabilities.setBrowserName("firefox"); break;
                default: System.out.println("Invalid Browser Name"); return;
            }

          driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);

        }

        if(p.getProperty("execution_env").equalsIgnoreCase("local"))
        {
            switch (br.toLowerCase())
            {
                case "chrome" : driver = new ChromeDriver(); break;
                case "edge" : driver = new EdgeDriver(); break;
                case "firefox" : driver = new FirefoxDriver(); break;
                default: System.out.println("Invalid Browser Name"); return;
            }
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
    }

    @AfterClass(groups = {"Sanity", "Regression", "Master"})
    public void tearDown() {
        driver.quit();
    }

    public String randomString() {
        String generatedstring = RandomStringUtils.randomAlphabetic(5);
        return generatedstring;
    }

    public String randomNumber() {
        String generatednumber = RandomStringUtils.randomNumeric(10);
        return generatednumber;
    }

    public String randomAlphaNumeric() {
        String generatedstring = RandomStringUtils.randomAlphabetic(3);
        String generatednumber = RandomStringUtils.randomNumeric(3);
        return (generatedstring+"@"+generatednumber);
    }
    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

        String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File targetFile = new File(targetFilePath);

        sourceFile.renameTo(targetFile);

        return targetFilePath;
    }
}




//For developing a Hybrid Driven Framework, focusing on creating a test case for Account Registration and adding logs using Log4j2.
//1) Test case: Account Registration
//1.1: Create BasePage under "pageObjects" which includes only a constructor. This constructor will be invoked by every Page Object Class constructor for reusability.
//1.2: Create Page Object Classes for HomePage, RegistrationPage under pageObjects package. These classes extend from BasePage.
//1.3: Create AccountRegistrationTest under the testCases package.
//1.4: Create BaseClass under the testBase package and copy reusable methods.
//1.5: Create reusable methods to generate random numbers and strings in BaseClass.
//2) Adding logs to test case (log4j2)
//2.1: Add log4j2.xml file under src/test/resources.
//2.2: Update BaseClass.
//2.3: Add log statements to AccountRegistrationTest.
//3) Run Tests on Desired Browser/Cross Browser/Parallel
//3.1: Create testng.xml file to run test cases and parameterize browser name and OS to BaseClass setup() method.
//3.2: Update BaseClass setup() method to launch the browser based on conditions.
//3.3: Maintain separate testng.xml files to run tests on multiple browsers in parallel.
//4) Read Common Values from config.properties File
//4.1: Add config.properties file under src/test/resources.
//4.2: Update BaseClass setup() method to add a script to load the config.properties file.
//4.3: Replace hardcoded values in test cases like URL, username, password, etc., with values from the config.properties file.
//5) Login Test Case
//5.1: Create and update page object classes: LoginPage, MyAccountPage (new classes), and update HomePage by adding the login link element.
//5.2: Create LoginTest.
//5.3: Add an entry to testng.xml.
//6) Data Driven Login Test
//6.1: Prepare test data in Excel and place the Excel file inside the testData folder.
//6.2: Create ExcelUtility class under the utilities package.
//6.3: Update MyAccountPage page object class to add the logout link element.
//6.4: Create DataProviders class in the utilities package to maintain data providers for data-driven tests.
//6.5: Create LoginDataDrivenTest under the testCases package.
//6.6: Add an entry in testng.xml file.
//7) Grouping Tests
//7.1: Add all test cases into specific groups (sanity, regression, master, etc.).
//7.2: Add BaseClass methods setup() and teardown() to all groups.
//7.3: Create a separate TestNG XML file (grouping.xml) to run groups and include the desired groups to execute.
//8) Add Extent Reports to Project
//8.1: Create ExtentReportUtility utility class under utilities package.
//8.2: Add captureScreen() method in BaseClass.
//8.3: Add ExtentReportUtility (Listener class) entry in testng.xml file.
//8.4: Make sure WebDriver is static in BaseClass, we refer same driver instance in ExtentReportUtility.
//9) Run Failed Tests
//test-output→testng-failed.xml
//10) Run Tests on Selenium Grid
//Grid Setup:
//Download selenium-server-4.15.0.jar and place it somewhere.
//Run below command in command prompt to start Selenium Grid:
//java -jar selenium-server-4.15.0.jar standalone
//URL to see sessions: http://localhost:4444/
//10.1: Add execution_env=local/remote in config.properties file under resources folder.
//10.2: Update setup() method in the BaseClass (capture execution environment from config.properties file then add required capabilities of OS & Browser in conditions).
//10.3: Run the tests from testing.xml
//11: Run Tests using Maven pom.xml, Command Prompt & run.bat file.
//12: Push the Code to Git & GitHub Repository
//13: Run Tests using Jenkins.