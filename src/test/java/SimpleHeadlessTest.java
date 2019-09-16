import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleHeadlessTest
{
	String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
	String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");

	String SAUCE_HEADLESS_URL = "https://ondemand.us-east-1.saucelabs.com/wd/hub";
	String SAUCE_HEADLESS_API = "https://us-east-1.saucelabs.com/rest/v1";

	public DesiredCapabilities capabilities;
	public RemoteWebDriver driver;
	public String sessionId;
	public SauceREST api;

	@Rule
	public TestName testName = new TestName();

	@Before
	public void setup() throws MalformedURLException
	{
		URL url = new URL(SAUCE_HEADLESS_URL);

		capabilities = new DesiredCapabilities();
		capabilities.setCapability("username", SAUCE_USERNAME);
		capabilities.setCapability("accessKey", SAUCE_ACCESS_KEY);
		capabilities.setCapability("platform", "any");
		capabilities.setCapability("browserName", "chrome");
		capabilities.setCapability("version", "latest");
		capabilities.setCapability("name", this.getClass().getSimpleName() + " " + testName.getMethodName());

		driver = new RemoteWebDriver(url, capabilities);
		sessionId = driver.getSessionId().toString();

		api = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY, DataCenter.US_EAST);
		api.jobFailed(sessionId);

	}

	@Test
	public void failingHeadlessTest()
	{
		driver.get("https://saucelabs.com");
		api.jobFailed(sessionId);
		assertThat(driver.getTitle()).contains("Hot Sauce");
	}

	@Test
	public void passingHeadlessTest()
	{
		driver.get("https://saucelabs.com");
		api.jobPassed(sessionId);
		assertThat(driver.getTitle()).contains("Sauce Labs");
	}

	@After
	public void cleanup()
	{
		driver.quit();
	}
}
