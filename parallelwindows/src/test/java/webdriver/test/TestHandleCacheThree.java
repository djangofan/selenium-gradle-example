package webdriver.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import qa.webdriver.util.WebDriverUtils;

public class TestHandleCacheThree extends WebDriverUtils {

	@BeforeClass
	public static void setUpTestHandleCacheTwoClass() {
		LOGGER.info("Finished setUpTestHandleCacheThreeClass");
	}

	public TestHandleCacheThree() {
		classlogger.info("Constructed TestHandleCacheThree");
	}

	@Before
	public void setUpTestHandleCacheThree() {
		testXOffset = 1100;
		initializeRemoteBrowser( "firefox", "localhost", 4444 );
		System.out.println("HandleCacheThree thread id = " + Thread.currentThread().getId());
		classlogger.info("Finished setUpTestHandleCacheThree");
	}

	@Test
	public void testHandleCacheTwo() {
		classlogger.info("Starting test testHandleCacheThree" );
		classlogger.info("Loading Window1 contents");
		driver.get( System.getProperty("testProtocol") + "://" + System.getProperty("testDomain") + ":" +
				System.getProperty("testPort") + System.getProperty("testUri") );
		waitTimer(4, 500);

		// Open Window2 via Window1
		classlogger.info("Opening Window2");
		scrollDownWithArrowKey();
		clickByIdWithJavascript( "btnNewWindow" );
		String h2 = handleNewWindow();
		waitTimer(2, 500);

		// Open Window3 and Window4 via Window2
		//driver.findElement( By.cssSelector("html body a:first-child") ).click();
		classlogger.info("Opening Window3");
		//driver.findElement(By.id("w3link")).click();
		clickByIdWithJavascript( "w3link" );
		String h3 = handleNewWindow();
		waitTimer(2, 500);
		driver.switchTo().window( h2 );
		//driver.findElement( By.cssSelector("html body a:last-child") ).click();
		classlogger.info("Opening Window4");
		//driver.findElement(By.id("w4link")).click();
		clickByIdWithJavascript( "w4link" );
		waitTimer(2, 500);
		String h4 = handleNewWindow();
		waitTimer(2, 500);

		// close Window4
		closeWindowByHandle( h4 );
		updateHandleCache();
		waitTimer(6, 500);

		// close Window3
		closeWindowByHandle( h3 );
		updateHandleCache();
		waitTimer(2, 500);

		// close Window2
		closeWindowByHandle( h2 );
		updateHandleCache();        
		waitTimer(2, 500);         

		classlogger.info( "Finished testHandleCacheThree" );
	} 

	@After
	public void tearDown() {
		// close main window handle
		driver.switchTo().window( mainHandle );
		driver.get("about:about");
		updateHandleCache();  
		waitTimer(6, 500);
		closeAllBrowserWindows(); 
		classlogger.info("Finished tearDownTestHandleCacheThree");
	}

	@AfterClass
	public static void tearDownClass() {
		LOGGER.info("Finished tearDownTestHandleCacheThreeClass");
	}

}
