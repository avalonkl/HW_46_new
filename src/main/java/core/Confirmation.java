package core;
import java.io.FileInputStream;
import java.io.Writer;
import java.util.Properties;
import org.openqa.selenium.By;
import com.aventstack.extentreports.ExtentTest;
class Confirmation {
	static Properties p = new Properties();
	static Writer report;
	static void validate(ExtentTest logger) throws Exception {
		p.load(new FileInputStream("./input.properties"));
		// 05 :: First Name
		Common.writeInfoLine("First Name", p.getProperty("fname_value"),
				Common.getValue(By.id(p.getProperty("fname_id"))), logger);
		// 06 :: Last Name
		Common.writeInfoLine("Last Name", p.getProperty("lname_value"),
				Common.getValue(By.id(p.getProperty("lname_id"))), logger);
		// 07 :: Email Name
		Common.writeInfoLine("Email", p.getProperty("email_value"),
				Common.getValue(By.id(p.getProperty("email_id"))), logger);
		// 08 :: Phone Name
		Common.writeInfoLine("Phone", p.getProperty("phone_value"),
				Common.getValue(By.id(p.getProperty("phone_id"))), logger);}
}