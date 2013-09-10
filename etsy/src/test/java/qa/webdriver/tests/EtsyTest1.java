package qa.webdriver.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import au.com.bytecode.opencsv.CSVReader;
import qa.webdriver.util.EtsySearchPage;
import qa.webdriver.util.WebDriverUtils;

@RunWith(Parameterized.class)
public class EtsyTest1 extends WebDriverUtils {

	private static String testName, searchString, ddMatch;

	public EtsyTest1( String tName, String sString, String dMatch ) {
		testName = tName;
		searchString = sString;
		ddMatch = dMatch;
		testXOffset = 0;
	}

	@Before
	public void setUp() {	
		if ( driver == null ) initializeRemoteBrowser( System.getProperty("browser"), 
				  System.getProperty("hubIP"), 
				  Integer.parseInt( System.getProperty("hubPort") ) );
		classlogger.info("Finished setUp EtsyTest1");
	}

	@Parameters(name = "{0}: {1}: {2}")
	public static Iterable<String[]> loadTestsFromFile1() {
		File tFile = loadGradleResource( System.getProperty("user.dir") + separator +  "build" +
				separator + "resources" + separator +  "test" + separator + "testdata1.csv" );
		List<String[]> rows = null;
		if ( tFile.exists() ) {
			CSVReader reader = null;
			try {
				reader = new CSVReader( new FileReader( tFile ), ',' );
				rows = reader.readAll();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		LOGGER.info("Finished loadTestsFromFile1()");
		return rows;
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
		gs.clickEtsyLogo(); // click Etsy logo
		classlogger.info("Page object test '{}' is done.", testName );
	}

	@Test
	public void testFluentPageObject() {    	
		classlogger.info("{} being run...", testName );
		driver.get( System.getProperty("testURL") );
		EtsySearchPage esp = new EtsySearchPage();
		esp.withFluent().clickSearchField()
		.setSearchString( searchString ).waitForTime(2, 1000)
		.selectItem( ddMatch ).clickSearchButton()
		.waitForTime(2, 1000).clickLogo(); //click Google logo
		classlogger.info("Fluent test '{}' is done.", testName );
	}

	@After
	public void cleanUp() {
		LOGGER.info("Finished cleanUp EtsyTest1");
		driver.get("about:about");
		waitTimer(2, 1000);
	}
	
	@AfterClass
	public static void tearDown() {
		LOGGER.info("Finished tearDown");
		driver.quit();
	}

}
