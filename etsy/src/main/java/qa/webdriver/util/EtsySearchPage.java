package qa.webdriver.util;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import static qa.webdriver.util.WebDriverUtils.*;

public class EtsySearchPage extends LoadableComponent<EtsySearchPage> {

	public ESPFluentInterface espfi;
	public static final String searchFieldName = "search_query";
	public static final String searchButtonName = "search_submit";
	public static final String suggestIons = "div.nav-search-text div#search-suggestions ul li";

	@FindBy(name = searchFieldName ) public WebElement searchField;
	@FindBy(name = searchButtonName ) public WebElement searchButton;

	public EtsySearchPage() {
		this.get(); // SlowLoadableComponent.get()
		LOGGER.info("EtsySearchPage constructor...");
		espfi = new ESPFluentInterface( this ); // use this only if you want to
	}
	
	/**
	 * Method: isLoaded()
	 * Overidden method from the LoadableComponent class.
	 * This method must contain an Assert on visibility of an element in order
	 *  to trigger another call of load() if element is not found.
	 * @return	void
	 * @throws	null
	 */
	@Override
	protected void isLoaded() throws Error {    	
		LOGGER.info("EtsySearchPage.isLoaded()...");
		boolean loaded = false;
		if ( !(searchField == null ) ) {
			try {
				if ( searchField.isDisplayed() ) {
					loaded = true;
				}
			} catch ( ElementNotVisibleException e ) {
				LOGGER.info( "Element may not be displayed yet." );
			}
		}
		Assert.assertTrue( "Etsy search field is not yet displayed.", loaded );
	}

	/**
	 * Method: load
	 * Overidden method from the LoadableComponent class.
	 * @return	void
	 * @throws	null
	 */
	@Override
	protected void load() {
		LOGGER.info("EtsySearchPage.load()...");
		PageFactory.initElements( driver, this ); // initialize WebElements on page
		waitTimer(2, 1000);
	}

	public void clickSearchButton() {
		if ( searchButton == null ) {
			searchButton = getElementByLocator( By.id( searchButtonName ) );
		} else {
		    try {
				searchButton.click();
			} catch ( ElementNotVisibleException e ) {
				LOGGER.info( "Element not visible exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				LOGGER.info( "Exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			}
		}
	}

	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void setSearchString( String sstr ) {
		clearAndType( searchField, sstr );
	}
	
	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void clickEtsyLogo() {
		LOGGER.info("Click Etsy logo...");
		WebElement logo = null;
		By locator = By.cssSelector( "h1#etsy a" );
		logo = getElementByLocator( locator );
		logo.click();
		waitTimer(2, 1000);
	}

	/**
	 * Method: withFluent
	 * Entrypoint for an object that can start a fluent action thread.
	 * @return	ESPFluentInterface
	 * @throws	null
	 */
	public ESPFluentInterface withFluent() {
		return espfi; 
	}	

	/**
	 * Method: selectInEtsyDropdown
	 * Selects element in dropdown using keydowns method (just for fun)
	 * as long as you typed a string first.  The thread sleeps and the 
	 * key arrow down are safe to comment out within the below block.
	 * @return	void
	 * @throws	StaleElementReferenceException
	 */
	public void selectInEtsyDropdown( String match ) {
		LOGGER.info("Selecting \"" + match + "\" from Etsy dynamic dropdown.");
		List<WebElement> allSuggestions = driver.findElements( By.cssSelector( suggestIons ) );  
		try {
			for ( WebElement suggestion : allSuggestions ) {
				Thread.sleep(600);
				searchField.sendKeys( Keys.ARROW_DOWN); // show effect of selecting item with keyboard arrow down
				if ( suggestion.getText().contains( match ) ) {
					suggestion.click();
					LOGGER.info("Found item and clicked it.");
					Thread.sleep(2000); // just to slow it down so human eyes can see it
					break;
				}
			}
		} catch ( StaleElementReferenceException see ) {
			LOGGER.info("Error while iterating dropdown list:\n" + see.getMessage() );
		} catch ( InterruptedException ie ) {
			ie.printStackTrace();
		}
		LOGGER.info("Finished select in Etsy dropdown.");
	}
	
	/**
	 *  Inner class
	 *  A fluent API interface that provides methods for calling normal
	 *  page object methods within this class. 
	 */	
	public class ESPFluentInterface {

		public ESPFluentInterface(EtsySearchPage EtsySearchPage) {
			LOGGER.info("Initialized fluent interface.");
		}

		public ESPFluentInterface clickLogo() {
			clickEtsyLogo();
			return this;
		}

		public ESPFluentInterface clickSearchButton() {
			searchButton.click();
			return this;
		}

		public ESPFluentInterface clickSearchField() {
			searchField.click();
			return this;
		}

		public ESPFluentInterface selectItem( String match ) {
			LOGGER.info("Selecting item in list using fluent API.");
			selectInEtsyDropdown( match );
			return this;
		}

		public ESPFluentInterface setSearchString( String sstr ) {
			clearAndType( searchField, sstr );
			return this;
		}		

		/**
		 *  Method: waitForTime
		 *  This method shows an example of a fluent method that provides
		 *   access to a method from an outside class.  In this case the
		 *   waitTimer in the utility class.  We cannot call waitTimer in
		 *   the middle of a fluent thread and so we provide a fluent method
		 *   here so that a wait can be defined.   
		 */
		public ESPFluentInterface waitForTime( int units, int ms ) {
			waitTimer( units, ms );
			return this;
		}

	}

}
