package org.brainteam.lunchbox.selenium;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.brainteam.lunchbox.selenium.pages.AdminUserDialog;
import org.brainteam.lunchbox.selenium.pages.AdminUsersPage;
import org.brainteam.lunchbox.selenium.pages.LoginPage;
import org.brainteam.lunchbox.selenium.pages.OrderPage;
import org.testng.annotations.Test;

public class SeleniumTest extends SeleniumBase {
	
	@Test(enabled=false) // FIXME: not working anymore (manumay, 7/2019)
	public void test() {
		testLoginEmpty();
		testLoginFailed();
		testLogin();
		
		testCancelNewUser();
	}

	public void testLoginEmpty() {
		LoginPage loginPage = new LoginPage(getDriver());
		assertEquals(loginPage.getNumberOfAlerts(), 0);
		loginPage.submitLogin();
		assertTrue(loginPage.isActive());
		assertEquals(loginPage.getNumberOfAlerts(), 0);
	}
	
	public void testLoginFailed() {
		LoginPage loginPage = new LoginPage(getDriver());
		assertEquals(loginPage.getNumberOfAlerts(), 0);
		loginPage.loginAs("user", "password");
		assertTrue(loginPage.isActive());
		assertEquals(loginPage.getNumberOfAlerts(), 1);
	}
	
	public void testLogin() {
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginAs("digadmin", "god");
		assertFalse(loginPage.isActive());
		
		OrderPage orderPage = new OrderPage(getDriver());
		assertTrue(orderPage.isActive());
	}
	
	public void testCancelNewUser() {
		OrderPage orderPage = new OrderPage(getDriver());
		orderPage.clickMenuItemAdminUsers();
		
		AdminUsersPage adminUsersPage = new AdminUsersPage(getDriver());
		assertTrue(adminUsersPage.isActive(getDriver()));
		adminUsersPage.clickAddUserButton();
		
		AdminUserDialog adminUserDialog = new AdminUserDialog(getDriver());
		assertTrue(adminUserDialog.isActive());
		assertFalse(adminUserDialog.isSaveButtonEnabled());
		
		adminUserDialog.clickCancel();
		assertTrue(adminUsersPage.isActive(getDriver()));
	}
	
}
