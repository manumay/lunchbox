package org.brainteam.lunchbox.selenium.pages;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
	
	private static final String TITLE = "Login";
	private static final String CLASS_LOGINALERT = "login-alert";
	private static final String NAME_J_USERNAME = "j_username";
	private static final String NAME_J_PASSWORD = "j_password";
	private static final String NAME_LOGIN = "login";

	public LoginPage(WebDriver driver) {
		super(driver);
		if (!isActive()) {
			throw new PageObjectException("incorrect page, expected login");
		}
	}

	public void loginAs(String loginUser, String loginPassword) {
        typeUserName(loginUser);
        typePassword(loginPassword);
        submitLogin();
    }
	
	public void typeUserName(String loginUser) {
		getDriver().findElement(By.name(NAME_J_USERNAME)).sendKeys(loginUser);
	}
	
	public void typePassword(String loginPassword) {
		getDriver().findElement(By.name(NAME_J_PASSWORD)).sendKeys(loginPassword);
	}
	
	public void submitLogin() {
		getDriver().findElement(By.name(NAME_LOGIN)).click();
	}
	
	public int getNumberOfAlerts() {
		List<WebElement> alerts = getDriver().findElements(By.className(CLASS_LOGINALERT));
		return alerts.size();
	}
	
	public boolean isActive() {
		return StringUtils.contains(getDriver().getTitle(), TITLE);
	}
	
}
