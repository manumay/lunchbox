package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.services:name=Hintergrundjob-Manager", description = "Verwaltung von Hintergrundjobs")
public class BackgroundExecutorConfigurationImpl extends PropertyConfiguration implements BackgroundExecutorConfiguration {
	
	static final String CONFIGURATION_FILENAME = "backgroundexecutor.properties";
	
	static final String CRON_IMPORTMENU_DEFAULT = "0 0 6 ? * MON";
	static final String CRON_IMPORTMENU_PROPERTY = "cronImportMenu";
	static final String CRON_LOCKOFFERS_DEFAULT = "0 0 15 * * ?";
	static final String CRON_LOCKOFFERS_PROPERTY = "cronLockOffers";
	static final String CRON_MAILBILLING_DEFAULT = "0 0 6 1 * ?";
	static final String CRON_MAILBILLING_PROPERTY = "cronMailBilling";
	static final String CRON_MAILORDER_DEFAULT = "0 0 16 * * ?";
	static final String CRON_MAILORDER_PROPERTY = "cronMailOrder";
	
	public BackgroundExecutorConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public String getCronScheduleImportMenu() {
		return getProperty(CRON_IMPORTMENU_PROPERTY, CRON_IMPORTMENU_DEFAULT);
	}
	
	@ManagedAttribute
	public void setCronScheduleImportMenu(String cron) {
		setProperty(CRON_IMPORTMENU_PROPERTY, cron);
		notifyConfigurationListeners(CRON_IMPORTMENU_PROPERTY, cron);
	}

	@Override
	@ManagedAttribute
	public String getCronScheduleLockOffers() {
		return getProperty(CRON_LOCKOFFERS_PROPERTY, CRON_LOCKOFFERS_DEFAULT);
	}
	
	@ManagedAttribute
	public void setCronScheduleLockOffers(String cron) {
		setProperty(CRON_LOCKOFFERS_PROPERTY, cron);
		notifyConfigurationListeners(CRON_LOCKOFFERS_PROPERTY, cron);
	}

	@Override
	@ManagedAttribute
	public String getCronScheduleMailBilling() {
		return getProperty(CRON_MAILBILLING_PROPERTY, CRON_MAILBILLING_DEFAULT);
	}
	
	@ManagedAttribute
	public void setCronScheduleMailBilling(String cron) {
		setProperty(CRON_MAILBILLING_PROPERTY, cron);
		notifyConfigurationListeners(CRON_MAILBILLING_PROPERTY, cron);
	}
	
	@Override
	@ManagedAttribute
	public String getCronScheduleMailOrder() {
		return getProperty(CRON_MAILORDER_PROPERTY, CRON_MAILORDER_DEFAULT);
	}
	
	@ManagedAttribute
	public void setCronScheduleMailOrder(String cron) {
		setProperty(CRON_MAILORDER_PROPERTY, cron);
		notifyConfigurationListeners(CRON_MAILORDER_PROPERTY, cron);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(CRON_IMPORTMENU_PROPERTY, CRON_IMPORTMENU_DEFAULT);
		p.put(CRON_LOCKOFFERS_PROPERTY, CRON_LOCKOFFERS_DEFAULT);
		p.put(CRON_MAILBILLING_PROPERTY, CRON_MAILBILLING_DEFAULT);
		p.put(CRON_MAILORDER_PROPERTY, CRON_MAILORDER_DEFAULT);
		return p;
	}

}
