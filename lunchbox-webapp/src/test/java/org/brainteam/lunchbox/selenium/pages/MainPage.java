package org.brainteam.lunchbox.selenium.pages;

import org.openqa.selenium.WebDriver;

public abstract class MainPage extends BasePage {
	
	private static final String MENUITEM_ORDER = "menuItemOrder";
	
	private static final String MENUITEM_REPORTS = "menuItemReports";
	private static final String MENUITEM_STATS_USER = "menuItemStatsUser";
	private static final String MENUITEM_STATS_SYSTEM = "menuItemStatsSystem";
	private static final String MENUITEM_REPORTDAY = "menuItemReportDay";
	private static final String MENUITEM_REPORTMONTH = "menuItemReportMonth";
	private static final String MENUITEM_REPORTBILLING = "menuItemReportBilling";
	
	private static final String MENUITEM_ADMIN = "menuItemAdmin";
	private static final String MENUITEM_ADMINOFFERS = "menuItemAdminOffers";
	private static final String MENUITEM_ADMINUSERS = "menuItemAdminUsers";
	private static final String MENUITEM_ADMINORDERS = "menuItemAdminOrders";
	private static final String MENUITEM_ADMINJOBS = "menuItemAdminJobs";
	private static final String MENUITEM_ADMINJOURNAL = "menuItemAdminJournal";
	private static final String MENUITEM_ADMINMEALS = "menuItemAdminMeals";
	private static final String MENUITEM_ADMINMENU = "menuItemAdminMenu";
	
	private static final String MENUITEM_ACCOUNT = "menuItemAccount";
	private static final String MENUITEM_PROFILE = "menuItemProfile";
	private static final String MENUITEM_LOGOUT = "menuItemLogout";

	public MainPage(WebDriver driver) {
		super(driver);
	}
	
	public void clickMenuItemOrder() {
		clickWhenClickable(MENUITEM_ORDER);
	}

	public void clickMenuItemUserStatistics() {
		clickWhenClickable(MENUITEM_REPORTS);
		clickWhenClickable(MENUITEM_STATS_USER);
	}
	
	public void clickMenuItemSystemStatistics() {
		clickWhenClickable(MENUITEM_REPORTS);
		clickWhenClickable(MENUITEM_STATS_SYSTEM);
	}
	
	public void clickMenuItemDailyReport() {
		clickWhenClickable(MENUITEM_REPORTS);
		clickWhenClickable(MENUITEM_REPORTDAY);
	}
	
	public void clickMenuItemMonthlyReport() {
		clickWhenClickable(MENUITEM_REPORTS);
		clickWhenClickable(MENUITEM_REPORTMONTH);
	}
	
	public void clickMenuItemBillingReport() {
		clickWhenClickable(MENUITEM_REPORTS);
		clickWhenClickable(MENUITEM_REPORTBILLING);
	}
	
	public void clickMenuItemAdminOffers() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINOFFERS);
	}
	
	public void clickMenuItemAdminUsers() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINUSERS);
	}
	
	public void clickMenuItemAdminOrders() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINORDERS);
	}
	
	public void clickMenuItemAdminJobs() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINJOBS);
	}
	
	public void clickMenuItemAdminJournal() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINJOURNAL);
	}
	
	public void clickMenuItemAdminMeals() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINMEALS);
	}
	
	public void clickMenuItemAdminMenu() {
		clickWhenClickable(MENUITEM_ADMIN);
		clickWhenClickable(MENUITEM_ADMINMENU);
	}
	
	public void clickMenuItemAdminEditProfile() {
		clickWhenClickable(MENUITEM_ACCOUNT);
		clickWhenClickable(MENUITEM_PROFILE);
	}
	
	public void clickMenuItemLogout() {
		clickWhenClickable(MENUITEM_ACCOUNT);
		clickWhenClickable(MENUITEM_LOGOUT);
	}
	
}
