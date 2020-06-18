package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.qameta.allure.*;

public class AllureReportTest {
	public WebDriver driver;
	static Properties p = new Properties();
	static String browser = System.getProperty("browser"); // -Dbrowser="firefox"

	@Step("Step [set value] - [{1}]")
	public void setValueTest(By by, String value) throws Exception {
		Common.setValue(by, value);
	};

	@Step("Step [validate element] - Expected: {0}/Actual: {1}")
	public void assertElement(String expected, String actual) {
		Assert.assertEquals(expected, actual);
	};

	@Test(priority = 1, description = "SignUp Page Validating")
	@Description("SignUp Page: Elements Validation")
	public void SignUpTest() throws Exception {
		// First Name
		setValueTest(By.id(p.getProperty("fname_id")), p.getProperty("fname_value"));
		assertElement(p.getProperty("fname_value"), Common.getValue(By.id(p.getProperty("fname_id"))));
		// Last Name
		setValueTest(By.id(p.getProperty("lname_id")), p.getProperty("lname_value"));
		assertElement(p.getProperty("lname_value"), Common.getValue(By.id(p.getProperty("lname_id"))));
		// Email Name
		setValueTest(By.id(p.getProperty("email_id")), p.getProperty("email_value"));
		assertElement(p.getProperty("email_value"), Common.getValue(By.id(p.getProperty("email_id"))));
		// Phone Name
		setValueTest(By.id(p.getProperty("phone_id")), p.getProperty("phone_value"));
		assertElement(p.getProperty("phone_value"), Common.getValue(By.id(p.getProperty("phone_id"))));
		// Submit
		Common.submit(By.id(p.getProperty("submit_id")));
	}

	@Test(dependsOnMethods = { "SignUpTest" }, priority = 2, description = "Confirmation Page Validating")
	@Description("Confirmation: Elemets Validation")
	public void ConfirmationTest() throws Exception {
		Common.waitTitlePage("Confirmation");
		// First Name
		assertElement(p.getProperty("fname_value"), Common.getValue(By.id(p.getProperty("fname_id"))));
		// Last Name
		assertElement(p.getProperty("lname_value"), Common.getValue(By.id(p.getProperty("lname_id"))));
		// Email Name
		assertElement(p.getProperty("email_value"), Common.getValue(By.id(p.getProperty("email_id"))));
		// Phone Name
		assertElement(p.getProperty("phone_value"), Common.getValue(By.id(p.getProperty("phone_id"))));
	}

	@BeforeClass
	@Step("Step [open browser]")
	public void setUp() throws Exception {
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
	@Step("Step [close browser]")
	public void tearDowm() throws Exception {
		Common.quit();
	}
}
