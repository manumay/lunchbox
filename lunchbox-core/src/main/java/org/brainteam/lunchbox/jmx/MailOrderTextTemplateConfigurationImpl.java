package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.templates:name=Bestellung-Mail", description = "Vorlage für Mail-Bestellung")
public class MailOrderTextTemplateConfigurationImpl extends PropertyConfiguration implements MailOrderTextTemplateConfiguration {

	private static final String CONFIGURATION_FILENAME = "mailordertexttemplate.properties";
	
	private static final String FILENAME_DEFAULT = "mailordertext.tpl";
	private static final String FILENAME_PROPERTY = "fileName";
	
	protected MailOrderTextTemplateConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public String getFileName() {
		return getProperty(FILENAME_PROPERTY, FILENAME_DEFAULT);
	}
	
	@ManagedAttribute
	public void setFileName(String fileName) {
		setProperty(FILENAME_PROPERTY, fileName);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(FILENAME_PROPERTY, FILENAME_DEFAULT);
		return p;
	}

}
