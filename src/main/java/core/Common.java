package core;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.*;

class Common {
	static WebDriver driver;
	static Writer report;
	static Properties p = new Properties();
	static String url;
	static String browser;

	static void getWebDriver(String browser) {
		Logger.getLogger("").setLevel(Level.OFF);
		String driverPath = "";

		switch (browser.toLowerCase()) {

		case "chrome": {
			if (getOS().toUpperCase().contains("MAC") || getOS().toUpperCase().contains("LINUX"))
				driverPath = "/usr/local/bin/chromedriver";
			else if (getOS().toUpperCase().contains("WINDOWS"))
				driverPath = "c:\\windows\\chromedriver.exe";
			else
				throw new IllegalArgumentException("Browser dosn't exist for this OS");
			System.setProperty("webdriver.chrome.driver", driverPath);
			System.setProperty("webdriver.chrome.silentOutput", "true"); // Chrome
			ChromeOptions option = new ChromeOptions(); // Chrome
			option.addArguments("disable-infobars"); // Chrome
			option.addArguments("--disable-notifications"); // Chrome

			driver = new ChromeDriver();
			break;
		}

		case "edge": {
			if (getOS().toUpperCase().contains("MAC"))
				driverPath = "/usr/local/bin/msedgedriver.sh";
			else if (getOS().toUpperCase().contains("WINDOWS"))
				driverPath = "c:\\windows\\msedgedriver.exe";
			else
				throw new IllegalArgumentException("Browser dosn't exist for this OS");
			System.setProperty("webdriver.edge.driver", driverPath);

			driver = new EdgeDriver();
			break;
		}

		case "firefox": {
			if (getOS().toUpperCase().contains("MAC") || getOS().toUpperCase().contains("LINUX"))
				driverPath = "/usr/local/bin/geckodriver.sh";
			else if (getOS().toUpperCase().contains("WINDOWS"))
				driverPath = "c:\\windows\\geckodriver.exe";
			else
				throw new IllegalArgumentException("Browser dosn't exist for this OS");
			System.setProperty("webdriver.gecko.driver", driverPath);

			driver = new FirefoxDriver();
			break;
		}

		case "safari": {
			if (!getOS().toUpperCase().contains("MAC"))
				throw new IllegalArgumentException("Browser dosn't exist for this OS");

			driver = new SafariDriver();
			break;
		}

		default:
			throw new WebDriverException("Unknown WebDriver");

		}
	}

	static String getOS() {
		return System.getProperty("os.name").toUpperCase();
	}

	static void open(String browser, String url) {
		getWebDriver(browser);
		driver.manage().window().maximize();
		driver.get(url);
	}

	static boolean isElementPresent(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (driver.findElements(by).size() == 1) {
			highlightElement(driver.findElement(by));
			unhighlightElement(driver.findElement(by));
			return true;
		} else
			return false;
	}

	static String getSize(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by))
			return driver.findElement(by).getSize().toString().replace(", ", "x");
		else
			return "null";
	}

	static String getLocation(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equals("Safari"))
			return "(0x0)";
		else {
			if (isElementPresent(by))
				return driver.findElement(by).getLocation().toString().replace(", ", "x");
			else
				return "null";
		}
	}

	public static void highlightElement(WebElement element) throws Exception {
		((RemoteWebDriver) driver).executeScript("arguments[0].setAttribute('style','border: solid 3px red');",
				element);
	}

	public static void unhighlightElement(WebElement element) throws Exception {
		Thread.sleep(50);
		((RemoteWebDriver) driver).executeScript("arguments[0].setAttribute('style','border: solid 0px red');",
				element);
	}

	static void setValue(By by, String value) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by))
			driver.findElement(by).sendKeys(value);
	}

	static String getValue(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by) && driver.findElement(by).getTagName().equalsIgnoreCase("input"))
			return driver.findElement(by).getAttribute("value").toString().trim();

		else if (isElementPresent(by) && driver.findElement(by).getTagName().equalsIgnoreCase("span"))
			return driver.findElement(by).getText().trim();
		else
			return "null";
	}

	static void submit(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by))
			driver.findElement(by).submit();
	}

	static String getBrowser() {
		String browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toString().trim();
		return browser.replaceFirst(String.valueOf(browser.charAt(0)), String.valueOf(browser.charAt(0)).toUpperCase());
	}

	static String getFileName() {
		String file = driver.getCurrentUrl().toString().trim();
		return file.substring(file.lastIndexOf('/') + 1);
	}

	static String getTitleName() {
		return driver.getTitle().toString().trim();
	}

	static void waitTitlePage(String title) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.titleIs(title));
	}

	static void quit() {
		driver.quit();
	}

	public static void checkCheckBox(By by) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by) && !driver.findElement(by).isSelected())
			driver.findElement(by).click();
	}

	public static void checkRadioButton(By by) throws Exception {
		if (isElementPresent(by) && !driver.findElement(by).isSelected())
			driver.findElement(by).click();
	}

	public static void selectDropDown(By by, String value) throws Exception {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (isElementPresent(by))
			new Select(driver.findElement(by)).selectByVisibleText(value);
	}

	public static String getScreenShot(String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("_yyyy-MM-dd_HH_mm").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	static void writeInfoLine(String element, String expected, String actual, ExtentTest logger) {
		String info = "Element [" + element + "] - Expected Result [" + expected + "] : Actual Result [" + actual + "]";
		if (expected.equals(actual))
			logger.log(Status.INFO, MarkupHelper.createLabel(info, ExtentColor.BLUE));
		else
			logger.log(Status.INFO, MarkupHelper.createLabel(info, ExtentColor.RED));
	}
}