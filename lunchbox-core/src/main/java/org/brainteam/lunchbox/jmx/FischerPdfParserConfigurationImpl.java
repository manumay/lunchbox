package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.jobs:name=Speisekarten-Parser", description = "Parsen einer Fischerkantine-Speisekarte")
public class FischerPdfParserConfigurationImpl extends PropertyConfiguration implements FischerPdfParserConfiguration {
	
	static final String CONFIGURATION_FILENAME = "fischerpdfparser.properties";
	
	static final Integer DEFAULTPRICE_DEFAULT = Integer.valueOf(95);
	static final String DEFAULTPRICE_PROPERTY = "defaultPrice";
	static final String MENU1TYPE_PROPERTY = "menu1Type";
	static final String MENU1TYPE_DEFAULT = "Menü 1";
	static final String MENU2TYPE_PROPERTY = "menu2Type";
	static final String MENU2TYPE_DEFAULT = "Menü 2";
	static final String MENU3TYPE_PROPERTY = "menu3Type";
	static final String MENU3TYPE_DEFAULT = "Menü 3";
	static final String SALADTYPE_PROPERTY = "saladType";
	static final String SALADTYPE_DEFAULT = "Salat";
	static final String SKIP_DEFAULT = ",KARFREITAG,OSTERMONTAG,PFINGSTMONTAG,BETRIEBSRUHE,FEIERTAG,KEIN ESSEN,Feiertag,Betriebsruhe";
	static final String SKIP_PROPERTY = "skip";
	
	public FischerPdfParserConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public Integer getDefaultPrice() {
		return getPropertyAsInteger(DEFAULTPRICE_PROPERTY, DEFAULTPRICE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setDefaultPrice(Integer defaultPrice) {
		setProperty(DEFAULTPRICE_PROPERTY, String.valueOf(defaultPrice));
	}

	@Override
	@ManagedAttribute
	public String getMenu1Type() {
		return getProperty(MENU1TYPE_PROPERTY, MENU1TYPE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMenu1Type(String type) {
		setProperty(MENU1TYPE_PROPERTY, type);
	}

	@Override
	@ManagedAttribute
	public String getMenu2Type() {
		return getProperty(MENU2TYPE_PROPERTY, MENU2TYPE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMenu2Type(String type) {
		setProperty(MENU2TYPE_PROPERTY, type);
	}

	@Override
	@ManagedAttribute
	public String getMenu3Type() {
		return getProperty(MENU3TYPE_PROPERTY, MENU3TYPE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMenu3Type(String type) {
		setProperty(MENU3TYPE_PROPERTY, type);
	}

	@Override
	@ManagedAttribute
	public String getSaladType() {
		return getProperty(SALADTYPE_PROPERTY, SALADTYPE_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSaladType(String type) {
		setProperty(SALADTYPE_PROPERTY, type);
	}
	
	@Override
	@ManagedAttribute
	public String[] getSkip() {
		return getPropertyAsStringArray(SKIP_PROPERTY, SKIP_DEFAULT);
	}
	
	@ManagedAttribute
	public String getSkipValue() {
		return getProperty(SKIP_PROPERTY, SKIP_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSkipValue(String skip) {
		setProperty(SKIP_PROPERTY, skip);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(DEFAULTPRICE_PROPERTY, String.valueOf(DEFAULTPRICE_DEFAULT));
		p.put(MENU1TYPE_PROPERTY, MENU1TYPE_DEFAULT);
		p.put(MENU2TYPE_PROPERTY, MENU2TYPE_DEFAULT);
		p.put(MENU3TYPE_PROPERTY, MENU3TYPE_DEFAULT);
		p.put(SALADTYPE_PROPERTY, SALADTYPE_DEFAULT);
		p.put(SKIP_PROPERTY, SKIP_DEFAULT);
		return p;
	}

}
