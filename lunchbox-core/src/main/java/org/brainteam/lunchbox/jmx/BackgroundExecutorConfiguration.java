package org.brainteam.lunchbox.jmx;


public interface BackgroundExecutorConfiguration extends ConfigurationProvider {
	
	String getCronScheduleImportMenu();
	
	String getCronScheduleLockOffers();
	
	String getCronScheduleMailBilling();
	
	String getCronScheduleMailOrder();

}
