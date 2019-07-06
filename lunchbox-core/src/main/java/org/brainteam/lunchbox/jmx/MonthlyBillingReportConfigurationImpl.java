package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.reports:name=Abrechnung-Excel", description = "Excel-Dokument mit Abrechnungsdaten")
public class MonthlyBillingReportConfigurationImpl extends PropertyConfiguration implements MonthlyBillingReportConfiguration {
	
	static final String CONFIGURATION_FILENAME = "pdfdailyreport.properties";
	
	static final String TITLE_DEFAULT = "Catering-Abrechnung";
	static final String TITLE_PROPERTY = "title";
	
	public MonthlyBillingReportConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public String getTitle() {
		return getProperty(TITLE_PROPERTY, TITLE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setTitle(String title) {
		setProperty(TITLE_PROPERTY, title);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(TITLE_PROPERTY, TITLE_DEFAULT);
		return p;
	}

}
