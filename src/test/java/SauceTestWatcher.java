import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SauceTestWatcher extends TestWatcher
{
	private ThreadLocal<String> sessionId;
	private SauceREST api;

	public SauceTestWatcher()
	{
		init(DataCenter.US);
	}

	public SauceTestWatcher(DataCenter dataCenter)
	{
		init(dataCenter);
	}

	public void init(DataCenter dataCenter)
	{
		String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
		String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
		api = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY, dataCenter);
		sessionId = new ThreadLocal<String>();
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId.set(sessionId);
	}

	@Override
	public Statement apply(Statement base, Description description)
	{
		return super.apply(base, description);
	}

	protected void succeeded(Description description)
	{
		System.out.println("PASSED test " + description);
		api.jobPassed(sessionId.get());
	}

	protected void failed(Throwable e, Description description)
	{
		System.out.println("FAILED test " + description);
		api.jobFailed(sessionId.get());
	}
}