package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.WebDriver;

public class StatisticsSystemPage extends MainPage {
	
	public StatisticsSystemPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void clickMenuItemSystemStatistics() {
		throw new PageObjectException("navigating to same page");
	}
	
}
