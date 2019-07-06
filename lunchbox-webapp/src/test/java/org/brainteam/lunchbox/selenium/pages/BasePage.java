package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	private WebDriver driver;
	
	public BasePage(WebDriver driver) {
		if (driver == null) {
			throw new IllegalArgumentException("driver may not be null");
		}
		this.driver = driver;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	protected void inputText(String name, String text) {
		By by = By.name(name);
		WebElement webElement = getDriver().findElement(by);
		webElement.sendKeys(text);
	}
	
	protected void clickWhenClickable(String id) {
		By by = By.id(id);
		new WebDriverWait(getDriver(), 2); // .until(ExpectedConditions.elementToBeClickable(by)); FIXME
		WebElement element = getDriver().findElement(by);
		element.click();
	}
	
}
