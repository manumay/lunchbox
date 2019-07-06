package org.brainteam.lunchbox.selenium;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class SeleniumBase {

	private static final String PARAMETER_JETTY_CONTEXT = "jetty.context";
	private static final String PARAMETER_JETTY_PORT = "jetty.port";
	private static final String PARAMETER_SELENIUM_BROWSER = "selenium.browser";
	private static final String DATASOURCE_NAME = "jdbc/DSLunchbox";
	private static final String WEBAPP_DIRECTORY = "src/main/webapp";
	
	private static final String SELENIUM_BROWSER_CHROME = "chrome";
	private static final String SELENIUM_BROWSER_FIREFOX = "firefox";
	private static final String SELENIUM_BROWSER_INTERNETEXPLORER = "internetexplorer";
	
	private static final String DEFAULT_JETTY_CONTEXT = "/";
	private static final String DEFAULT_JETTY_PORT = "9999";
	
	private Server server = null;
	private WebDriver driver = null;
	
	@BeforeSuite
	public void startWebApp(ITestContext context) throws Exception {		
	    server = new Server(getJettyPort(context));
	    configureServer(context, server);
	    server.start();
	}
	
	protected void configureServer(ITestContext context, Server server) throws NamingException {
		DataSource dataSource = createDataSource(context);
		Resource resource = new Resource(DATASOURCE_NAME, dataSource);
		server.setAttribute(DATASOURCE_NAME, resource);
		
		WebAppContext ctx = new WebAppContext(WEBAPP_DIRECTORY, getJettyContext(context));
		server.setHandler(ctx);
	}
	
	protected DataSource createDataSource(ITestContext context) {
		EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName("memory:Lunchbox;create=true");
		dataSource.setDataSourceName(DATASOURCE_NAME);
		dataSource.setPassword("lunchbox");
		dataSource.setUser("lunchbox");
		return dataSource;
	}
	
//	@BeforeSuite(dependsOnMethods="startWebApp")
	public void startSelenium(ITestContext context) {
		String browserName = getParameter(context, PARAMETER_SELENIUM_BROWSER, SELENIUM_BROWSER_FIREFOX);
		driver = createWebDriver(browserName);
		
		String url = "http://localhost:" + getJettyPort(context) + getJettyContext(context);
		driver.get(url);
	}
	
	protected WebDriver createWebDriver(String browserName) {
		switch (browserName) {
		case SELENIUM_BROWSER_CHROME:
			return new ChromeDriver();
		case SELENIUM_BROWSER_INTERNETEXPLORER:
			return new InternetExplorerDriver();
		case SELENIUM_BROWSER_FIREFOX:		
		default:
			return new FirefoxDriver();
		}
	}
	
	@AfterSuite
	public void stopWebApp() throws Exception {
		server.stop();
		server = null;
	}

//	@AfterSuite
	public void stopSelenium() {
		if (driver != null) {
			driver.close();
		}
		driver = null;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	private static Integer getJettyPort(ITestContext context) {
		String port = getParameter(context, PARAMETER_JETTY_PORT, DEFAULT_JETTY_PORT);
		return Integer.valueOf(port);
	}
	
	private static String getJettyContext(ITestContext context) {
		return getParameter(context, PARAMETER_JETTY_CONTEXT, DEFAULT_JETTY_CONTEXT);
	}
	
	private static String getParameter(ITestContext context, String parameterName, String defaultValue) {
		String suiteValue = context.getSuite().getParameter(parameterName);
		return suiteValue != null ? suiteValue : defaultValue;
	}

}
