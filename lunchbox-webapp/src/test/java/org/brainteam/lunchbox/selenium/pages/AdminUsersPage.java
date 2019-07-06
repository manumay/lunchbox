package org.brainteam.lunchbox.selenium.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

public class AdminUsersPage extends MainPage {

	private static final String TITLE = "Benutzer";
	private static final String ID_ADD_USER_BUTTON = "addUser";
	
	public AdminUsersPage(WebDriver driver) {
		super(driver);
		if (!isActive(driver)) {
			throw new PageObjectException("incorrect page");
		}
	}
	
	public void clickAddUserButton() {
		clickWhenClickable(ID_ADD_USER_BUTTON);
	}
	
	public boolean isActive(WebDriver driver) {
		return StringUtils.endsWith(driver.getTitle(), TITLE);
	}

}
