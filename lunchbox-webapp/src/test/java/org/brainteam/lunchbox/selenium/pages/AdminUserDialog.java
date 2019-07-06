package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminUserDialog extends BasePage {
	
	private static final String ID_LOGINNAME_INPUT = "loginNameInput";
	private static final String ID_SAVE_USER_BUTTON = "saveUser";
	private static final String ID_CANCEL_USER_BUTTON = "cancelUser";

	public AdminUserDialog(WebDriver driver) {
		super(driver);
	}
	
	public void enterLoginName(String loginName) {
		inputText(ID_LOGINNAME_INPUT, loginName);
	}
	
	public void clickSave() {
		clickWhenClickable(ID_SAVE_USER_BUTTON);
	}

	public void clickCancel() {
		clickWhenClickable(ID_CANCEL_USER_BUTTON);
	}
	
	public boolean isSaveButtonEnabled() {
		return getDriver().findElement(By.id(ID_SAVE_USER_BUTTON)).isEnabled();
	}
	
	public boolean isActive() {
		return true; // FIXME
	}
	
}
