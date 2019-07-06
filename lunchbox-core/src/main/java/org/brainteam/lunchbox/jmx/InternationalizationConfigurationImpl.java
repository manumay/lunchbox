package org.brainteam.lunchbox.jmx;

import java.util.Locale;
import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.services:name=Sprache&Währung", description = "Einstellungen zur Sprache und Währung")
public class InternationalizationConfigurationImpl extends PropertyConfiguration implements InternationalizationConfiguration {
	
	static final String CONFIGURATION_FILENAME = "translator.properties";
	
	public static final String EXCELCURRENCYFORMAT_DEFAULT = "#,##0.00_ \"€\"";
	static final String EXCELCURRENCYFORMAT_PROPERTY = "excelCurrencyFormat";
	static final String LANGUAGETAG_DEFAULT = Locale.GERMANY.toLanguageTag();
	static final String LANGUAGETAG_PROPERTY = "languageTag";
	
	public InternationalizationConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	public Locale getLocale() {
		String languageTag = getLanguageTag();
		return Locale.forLanguageTag(languageTag);
	}
	
	@ManagedAttribute
	public String getLanguageTag() {
		return getProperty(LANGUAGETAG_PROPERTY, LANGUAGETAG_DEFAULT);
	}
	
	@ManagedAttribute
	public void setLanguageTag(String languageTag) {
		setProperty(LANGUAGETAG_PROPERTY, languageTag);
	}
	
	@Override
	@ManagedAttribute
	public String getExcelCurrencyFormat() {
		return getProperty(EXCELCURRENCYFORMAT_PROPERTY, EXCELCURRENCYFORMAT_DEFAULT);
	}
	
	@ManagedAttribute
	public void setExcelCurrencyFormat(String format) {
		setProperty(EXCELCURRENCYFORMAT_PROPERTY, format);
	}

	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(EXCELCURRENCYFORMAT_PROPERTY, EXCELCURRENCYFORMAT_DEFAULT);
		p.put(LANGUAGETAG_PROPERTY, LANGUAGETAG_DEFAULT);
		return p;
	}

}
