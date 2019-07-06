package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.reports:name=Bestellung-PDF", description = "PDF-Dokument mit Bestellungsdaten")
public class DailyOrderPdfReportConfigurationImpl extends PropertyConfiguration implements DailyOrderPdfReportConfiguration {
	
	static final String CONFIGURATION_FILENAME = "pdfdailyreport.properties";
	
	static final String INFO_DEFAULT = "";
	static final String INFO_PROPERTY = "info";
	
	public DailyOrderPdfReportConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public String getInfo() {
		return getProperty(INFO_PROPERTY, INFO_DEFAULT);
	}
	
	@ManagedAttribute
	public void setInfo(String info) {
		setProperty(INFO_PROPERTY, info);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(INFO_PROPERTY, INFO_DEFAULT);
		return p;
	}

}
