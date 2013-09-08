package qa.webdriver.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import qa.webdriver.util.EtsySearchPage;
import qa.webdriver.util.WebDriverUtils;

// this class runs a single test for debugging purposes
public class DebugSingle extends WebDriverUtils {

	private static String testName, searchString, ddMatch;

	public DebugSingle() {
		testName = "debugTest";
		searchString = "daft punk";
		ddMatch = "tshirt";
		testXOffset = 0;
	}

	@Before
	public void setUp() {	
		if ( driver == null ) initializeRemoteBrowser( System.getProperty("browser"), 
				  System.getProperty("hubIP"), 
				  Integer.parseInt( System.getProperty("hubPort") ) );
		LOGGER.info("Finished setUp");
	}

	@Test
	public void testWithPageObject() {
		classlogger.info("{} being run...", testName );
		driver.get( System.getProperty("testURL") );
		EtsySearchPage gs = new EtsySearchPage();
		gs.setSearchString( searchString );
		gs.selectInEtsyDropdown( ddMatch );  
		gs.clickSearchButton();
		waitTimer(2, 1000);
		getElementByLocator( By.cssSelector( "h1#etsy a" ) ).click(); // click Google logo
		classlogger.info("Page object test '{}' is done.", testName );
	}

	@Test
	public void testFluentPageObject() {    	
		classlogger.info("{} being run...", testName );
		driver.get( System.getProperty("testURL") + "webhp?hl=en&tab=ww" );
		EtsySearchPage esp = new EtsySearchPage();
		esp.withFluent().clickSearchField()
		.setSearchString( searchString ).waitForTime(2, 1000)
		.selectItem( ddMatch ).clickSearchButton()
		.waitForTime(2, 1000).clickLogo(); //click Google logo
		classlogger.info("Fluent test '{}' is done.", testName );
	}

	@After
	public void cleanUp() {
		LOGGER.info("Finished cleanUp");
		driver.get("about:about");
		waitTimer(2, 1000);
	}
	
	@AfterClass
	public static void tearDown() {
		LOGGER.info("Finished tearDown");
		driver.quit();
	}

}
