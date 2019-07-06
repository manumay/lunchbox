package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.WebDriver;

public class OrderPage extends MainPage {

	private static final String TITLE = "Bestellung";
	
	public OrderPage(WebDriver driver) {
		super(driver);
	}
	
	public boolean isActive() {
		return getDriver().getTitle().endsWith(TITLE);
	}
	
}
