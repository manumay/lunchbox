package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.WebDriver;

public class StatisticsUserPage extends MainPage {
	
	public StatisticsUserPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void clickMenuItemUserStatistics() {
		throw new PageObjectException("navigating to same page");
	}

}