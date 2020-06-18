package core;

import java.io.FileInputStream;
import java.util.Properties;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.*;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportsTest {
	public WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	static Properties p = new Properties();
	static String browser = System.getProperty("browser"); // -Dbrowser="firefox"

	@Test(priority = 0)
	public void SignUpTest() throws Exception {
		logger = extent.createTest("SignUp Page Validation");
		SignUp.validate(logger);
		// First Name
		Assert.assertEquals(p.getProperty("fname_value"), Common.getValue(By.id(p.getProperty("fname_id"))));
		// Last Name
		Assert.assertEquals(p.getProperty("lname_value"), Common.getValue(By.id(p.getProperty("lname_id"))));
		// Email Name
		Assert.assertEquals(p.getProperty("email_value"), Common.getValue(By.id(p.getProperty("email_id"))));
		// Phone Name
		Assert.assertEquals(p.getProperty("phone_value"), Common.getValue(By.id(p.getProperty("phone_id"))));
		// Submit
		Common.submit(By.id(p.getProperty("submit_id")));
	}

	@Test(dependsOnMethods = { "SignUpTest" }, priority = 1)
	public void ConfirmationTest() throws Exception {
		Common.waitTitlePage("Confirmation");
		logger = extent.createTest("Confirmation Page Validation");
		Confirmation.validate(logger);
		// First Name
		Assert.assertEquals(p.getProperty("fname_value"), Common.getValue(By.id(p.getProperty("fname_id"))));
		// Last Name
		Assert.assertEquals(p.getProperty("lname_value"), Common.getValue(By.id(p.getProperty("lname_id"))));
		// Email Name
		Assert.assertEquals(p.getProperty("email_value"), Common.getValue(By.id(p.getProperty("email_id"))));
		// Phone Name
		Assert.assertEquals(p.getProperty("phone_value"), Common.getValue(By.id(p.getProperty("phone_id"))));
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		if (browser == null) {
			System.err.println("Please provide browser: -Dbrowser=firefox");
			System.exit(0);
		} else if (!browser.equalsIgnoreCase("chrome") && !browser.equalsIgnoreCase("firefox")
				&& !browser.equalsIgnoreCase("safari") && !browser.equalsIgnoreCase("edge")) {
			System.err.println("Framework supports only: Chrome, Firefox, Safari or Edge");
			System.exit(0);
		}
		SignUp.open(browser);
		p.load(new FileInputStream("./input.properties"));
	}

	@AfterClass
	public void afterClass() throws Exception {
		Common.quit();
	}

	@BeforeTest
	public void startReport() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/extentreports.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("User Name", "John Smith");
		extent.setSystemInfo("Browser", browser);
		htmlReporter.config().setDocumentTitle("Sign Up");
		htmlReporter.config().setReportName("Sign Up Validation"); // DARK
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {
		String screenshotPath = Common.getScreenShot(result.getName());
		logger.addScreenCaptureFromPath(screenshotPath);
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		}
	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}
}
