package utilities;
//import java.net.URL;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;
    String repName;

    public void onStart(ITestContext testContext) {

        /*SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date dt = new Date();
        String currentdatetimestamp = df.format(dt);
        */

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // time stamp
        repName = "Test-Report-" + timeStamp + ".html";
        sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName); // specify location of the report

        sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
        sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");

        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }
        public void onTestSuccess (ITestResult result) {
            test = extent.createTest(result.getTestClass().getName());
            test.assignCategory(result.getMethod().getGroups()); // to display groups in report
            test.log(Status.PASS, result.getName() + " got successfully executed");
        }
        public void onTestFailure(ITestResult result) {
            test = extent.createTest(result.getTestClass().getName());
            test.assignCategory(result.getMethod().getGroups());

            test.log(Status.FAIL, result.getName() + " got failed");
            test.log(Status.INFO, result.getThrowable().getMessage());

            try {
                String imgPath = new BaseClass().captureScreen(result.getName());
                test.addScreenCaptureFromPath(imgPath);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        public void onTestSkipped(ITestResult result) {
            test = extent.createTest(result.getTestClass().getName());
            test.assignCategory(result.getMethod().getGroups());
            test.log(Status.SKIP, result.getName() + " got skipped");
            test.log(Status.INFO, result.getThrowable().getMessage());
        }

        public void onFinish(ITestContext testContext) {
            extent.flush();
            String pathofExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
            File extentReport = new File(pathofExtentReport);
            try {
                Desktop.getDesktop().browse(extentReport.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            try {
                // Construct the URL for the Extent report
                URL url = new URL("file:///" + System.getProperty("user.dir") + "\\reports\\" + repName);

                // Create an ImageHtmlEmail object for sending HTML emails with attachments
                ImageHtmlEmail email = new ImageHtmlEmail();

                // Configure the email content source resolver to handle embedded images from the report
                email.setDataSourceResolver(new DataSourceUrlResolver(url));

                // Set email server details (replace with your own)
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("your_email@gmail.com", "your_password")); // Replace with your credentials
                email.setSSLOnConnect(true);

                // Set sender and recipient email addresses (replace with your own)
                email.setFrom("your_email@gmail.com");
                email.setSubject("Test Results");
                email.setMsg("Please find Attached Report...");
                email.addTo("recipient_email@gmail.com");

                // Attach the Extent report as a file
                email.attach(url, "extent report", "please check report...");

                // Send the email
                email.send();

            } catch (Exception e) {
                e.printStackTrace();
            }

             */
        }
}

