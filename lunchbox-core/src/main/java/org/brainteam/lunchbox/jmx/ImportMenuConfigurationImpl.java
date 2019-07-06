package org.brainteam.lunchbox.jmx;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.jobs:name=Speisekarten-Import", description = "Automatischer Import der Speisekarte")
public class ImportMenuConfigurationImpl extends PropertyConfiguration implements ImportMenuConfiguration {
	
	static final String CONFIGURATION_FILENAME = "importmenu.properties";
	
	static final String URLSPEC_DEFAULT = "http://www.fischer-schramberg.de/kommend.pdf";
	public static final URL URL_DEFAULT;
	static {
		try {
			URL_DEFAULT = new URL(URLSPEC_DEFAULT);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	static final String URL_PROPERTY = "url";
	
	public ImportMenuConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	public URL getURL() {
		return getPropertyAsURL(URL_PROPERTY, URL_DEFAULT);
	}
	
	@ManagedAttribute
	public String getUrlValue() {
		return getProperty(URL_PROPERTY, URLSPEC_DEFAULT);
	}
	
	@ManagedAttribute
	public void setUrlValue(String url) {
		setProperty(URL_PROPERTY, url);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(URL_PROPERTY, URLSPEC_DEFAULT);
		return p;
	}

}
