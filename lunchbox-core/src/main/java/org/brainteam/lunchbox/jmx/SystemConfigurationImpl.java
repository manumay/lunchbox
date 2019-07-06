package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.config:name=System", description = "Allgemeine Einstellungen")
public class SystemConfigurationImpl extends PropertyConfiguration implements SystemConfiguration {
	
	private static final String CONFIGURATION_FILENAME = "system.properties";
	
	private static final String COMPANYNAME_DEFAULT = "";
	private static final String COMPANYNAME_PROPERTY = "companyName";
	public static final String LUNCHTIME_DEFAULT = "13";
	private static final String LUNCHTIME_PROPERTY = "lunchtime";
	private static final String WEEKDAYS_ONLY_DEFAULT = "true";
	private static final String WEEKDAYS_ONLY_PROPERTY = "weekdaysOnly";

	protected SystemConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}
	
	@Override
	@ManagedAttribute
	public String getCompanyName() {
		return getProperty(COMPANYNAME_PROPERTY, COMPANYNAME_DEFAULT);
	}
	
	@ManagedAttribute
	public void setCompanyName(String companyName) {
		setProperty(COMPANYNAME_PROPERTY, companyName);
	}
	
	@Override
	@ManagedAttribute
	public String getLunchtime() {
		return getProperty(LUNCHTIME_PROPERTY, LUNCHTIME_DEFAULT);
	}
	
	@ManagedAttribute
	public void setLunchtime(String lunchtime) {
		setProperty(LUNCHTIME_PROPERTY, lunchtime);
	}
	
	@Override
	@ManagedAttribute
	public boolean isWeekdaysOnly() {
		return getPropertyAsBoolean(WEEKDAYS_ONLY_PROPERTY, Boolean.valueOf(WEEKDAYS_ONLY_DEFAULT)).booleanValue();
	}
	
	@ManagedAttribute
	public void setWeekdaysOnly(boolean weekdaysOnly) {
		setProperty(WEEKDAYS_ONLY_PROPERTY, Boolean.valueOf(weekdaysOnly).toString());
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.setProperty(COMPANYNAME_PROPERTY, COMPANYNAME_DEFAULT);
		p.setProperty(LUNCHTIME_PROPERTY, LUNCHTIME_DEFAULT);
		p.setProperty(WEEKDAYS_ONLY_PROPERTY, WEEKDAYS_ONLY_DEFAULT);
		return p;
	}
	

}
