package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.jobs:name=Mail-Versand", description = "Konfiguration für automatischen E-Mail-Versand")
public class MailServiceConfigurationImpl extends PropertyConfiguration implements MailServiceConfiguration {
	
	private static final String CONFIGURATION_FILENAME = "mailservice.properties";
	
	private static final String MAILBILLING_BCC_DEFAULT = "";
	private static final String MAILBILLING_BCC_PROPERTY = "mailBillingBcc";
	private static final String MAILBILLING_CC_DEFAULT = "";
	private static final String MAILBILLING_CC_PROPERTY = "mailBillingCc";
	private static final String MAILBILLING_INFO_DEFAULT = "";
	private static final String MAILBILLING_INFO_PROPERTY = "mailBillingInfo";
	private static final String MAILBILLING_SUBJECT_DEFAULT = "Abrechnung - $nameOfMonth $year";
	private static final String MAILBILLING_SUBJECT_PROPERTY = "mailBillingSubject";
	private static final String MAILBILLING_TO_DEFAULT = "";
	private static final String MAILBILLING_TO_PROPERTY = "mailBillingTo";
	private static final String MAILORDERS_BCC_DEFAULT = "";
	private static final String MAILORDERS_BCC_PROPERTY = "mailOrdersBcc";
	private static final String MAILORDERS_CC_DEFAULT = "";
	private static final String MAILORDERS_CC_PROPERTY = "mailOrdersCc";
	private static final String MAILORDERS_INFO_DEFAULT = "";
	private static final String MAILORDERS_INFO_PROPERTY = "mailOrdersInfo";
	private static final String MAILORDERS_SUBJECT_DEFAULT = "Bestellung - $orderDate";
	private static final String MAILORDERS_SUBJECT_PROPERTY = "mailOrdersSubject";
	private static final String MAILORDERS_TO_DEFAULT = "";
	private static final String MAILORDERS_TO_PROPERTY = "mailOrdersTo";

	protected MailServiceConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}
	
	@Override
	public String[] getMailBillingBCCAsArray() {
		return getPropertyAsStringArray(MAILBILLING_BCC_PROPERTY, MAILBILLING_BCC_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailBillingBCC() {
		return getProperty(MAILBILLING_BCC_PROPERTY, MAILBILLING_BCC_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailBillingBCC(String value) {
		setProperty(MAILBILLING_BCC_PROPERTY, MAILBILLING_BCC_DEFAULT);
	}
	
	@Override
	public String[] getMailBillingCCAsArray() {
		return getPropertyAsStringArray(MAILBILLING_CC_PROPERTY, MAILBILLING_CC_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailBillingCC() {
		return getProperty(MAILBILLING_CC_PROPERTY, MAILBILLING_CC_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailBillingCC(String value) {
		setProperty(MAILBILLING_CC_PROPERTY, MAILBILLING_CC_DEFAULT);
	}
	
	@Override
	@ManagedAttribute
	public String getMailBillingInfo() {
		return getProperty(MAILBILLING_INFO_PROPERTY, MAILBILLING_INFO_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailBillingInfo(String info) {
		setProperty(MAILBILLING_INFO_PROPERTY, info);
	}
	
	@Override
	@ManagedAttribute
	public String getMailBillingSubject() {
		return getProperty(MAILBILLING_SUBJECT_PROPERTY, MAILBILLING_SUBJECT_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailBillingSubject(String subject) {
		setProperty(MAILBILLING_SUBJECT_PROPERTY, subject);
	}
	
	@Override
	public String[] getMailBillingToAsArray() {
		return getPropertyAsStringArray(MAILBILLING_TO_PROPERTY, MAILBILLING_TO_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailBillingTo() {
		return getProperty(MAILBILLING_TO_PROPERTY, MAILBILLING_TO_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailBillingTo(String value) {
		setProperty(MAILBILLING_TO_PROPERTY, MAILBILLING_TO_DEFAULT);
	}
	
	@Override
	public String[] getMailOrdersBCCAsArray() {
		return getPropertyAsStringArray(MAILORDERS_BCC_PROPERTY, MAILORDERS_BCC_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailOrdersBCC() {
		return getProperty(MAILORDERS_BCC_PROPERTY, MAILORDERS_BCC_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailOrdersBCC(String value) {
		setProperty(MAILORDERS_BCC_PROPERTY, MAILORDERS_BCC_DEFAULT);
	}
	
	@Override
	public String[] getMailOrdersCCAsArray() {
		return getPropertyAsStringArray(MAILORDERS_CC_PROPERTY, MAILORDERS_CC_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailOrdersCC() {
		return getProperty(MAILORDERS_CC_PROPERTY, MAILORDERS_CC_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailOrdersCC(String value) {
		setProperty(MAILORDERS_CC_PROPERTY, MAILORDERS_CC_DEFAULT);
	}
	
	@Override
	@ManagedAttribute
	public String getMailOrdersInfo() {
		return getProperty(MAILORDERS_INFO_PROPERTY, MAILORDERS_INFO_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailOrdersInfo(String info) {
		setProperty(MAILORDERS_INFO_PROPERTY, info);
	}
	
	@Override
	@ManagedAttribute
	public String getMailOrdersSubject() {
		return getProperty(MAILORDERS_SUBJECT_PROPERTY, MAILORDERS_SUBJECT_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailOrdersSubject(String subject) {
		setProperty(MAILORDERS_SUBJECT_PROPERTY, subject);
	}
	
	@Override
	public String[] getMailOrdersToAsArray() {
		return getPropertyAsStringArray(MAILORDERS_TO_PROPERTY, MAILORDERS_TO_DEFAULT);
	}
	
	@ManagedAttribute
	public String getMailOrdersTo() {
		return getProperty(MAILORDERS_TO_PROPERTY, MAILORDERS_TO_DEFAULT);
	}
	
	@ManagedAttribute
	public void setMailOrdersTo(String value) {
		setProperty(MAILORDERS_TO_PROPERTY, MAILORDERS_TO_DEFAULT);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(MAILBILLING_INFO_PROPERTY, MAILBILLING_INFO_DEFAULT);
		p.put(MAILBILLING_SUBJECT_PROPERTY, MAILBILLING_SUBJECT_DEFAULT);
		p.put(MAILBILLING_BCC_PROPERTY, MAILBILLING_BCC_DEFAULT);
		p.put(MAILBILLING_CC_PROPERTY, MAILBILLING_CC_DEFAULT);
		p.put(MAILBILLING_TO_PROPERTY, MAILBILLING_TO_DEFAULT);
		p.put(MAILORDERS_INFO_PROPERTY, MAILORDERS_INFO_DEFAULT);
		p.put(MAILORDERS_SUBJECT_PROPERTY, MAILORDERS_SUBJECT_DEFAULT);
		p.put(MAILORDERS_BCC_PROPERTY, MAILORDERS_BCC_DEFAULT);
		p.put(MAILORDERS_CC_PROPERTY, MAILORDERS_CC_DEFAULT);
		p.put(MAILORDERS_TO_PROPERTY, MAILORDERS_TO_DEFAULT);
		return p;
	}

}
