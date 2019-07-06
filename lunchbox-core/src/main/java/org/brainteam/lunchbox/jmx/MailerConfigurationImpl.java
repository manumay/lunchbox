package org.brainteam.lunchbox.jmx;

import java.util.Properties;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "org.brainteam.lunchbox.services:name=Mailer", description = "Mail-Service")
public class MailerConfigurationImpl extends PropertyConfiguration implements MailerConfiguration {
	
	private static final String CONFIGURATION_FILENAME = "mailer.properties";
	
	private static final String FROM_DEFAULT = "";
	private static final String FROM_PROPERTY = "from";
	private static final String SMTP_HOSTNAME_DEFAULT = "";
	private static final String SMTP_HOSTNAME_PROPERTY = "smtpHostName";
	private static final String SMTP_PASSWORD_DEFAULT = "";
	private static final String SMTP_PASSWORD_PROPERTY = "smtpPassword";
	private static final Integer SMTP_PORT_DEFAULT = Integer.valueOf(25);
	private static final String SMTP_PORT_PROPERTY = "smtpPort";
	private static final String SMTP_USERNAME_DEFAULT = "";
	private static final String SMTP_USERNAME_PROPERTY = "smtpUserName";
	
	public MailerConfigurationImpl() {
		super(CONFIGURATION_FILENAME);
	}

	@Override
	@ManagedAttribute
	public String getFrom() {
		return getProperty(FROM_PROPERTY, FROM_DEFAULT);
	}
	
	@ManagedAttribute
	public void setFrom(String from) {
		setProperty(FROM_PROPERTY, from);
	}

	@Override
	@ManagedAttribute
	public String getSmtpHostName() {
		return getProperty(SMTP_HOSTNAME_PROPERTY, SMTP_HOSTNAME_DEFAULT);
	}

	@ManagedAttribute
	public void setSmtpHostName(String hostName) {
		setProperty(SMTP_HOSTNAME_PROPERTY, hostName);
	}
	
	@Override
	@ManagedAttribute
	public String getSmtpPassword() {
		return getProperty(SMTP_PASSWORD_PROPERTY, SMTP_PASSWORD_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSmtpPassword(String password) {
		setProperty(SMTP_PASSWORD_PROPERTY, password);
	}

	@Override
	@ManagedAttribute
	public Integer getSmtpPort() {
		return getPropertyAsInteger(SMTP_PORT_PROPERTY, SMTP_PORT_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSmtpPort(Integer port) {
		setProperty(SMTP_PORT_PROPERTY, String.valueOf(port));
	}

	@Override
	@ManagedAttribute
	public String getSmtpUserName() {
		return getProperty(SMTP_USERNAME_PROPERTY, SMTP_USERNAME_DEFAULT);
	}
	
	@ManagedAttribute
	public void setSmtpUserName(String userName) {
		setProperty(SMTP_USERNAME_PROPERTY, userName);
	}
	
	@Override
	protected Properties getDefaultProperties() {
		Properties p = super.getDefaultProperties();
		p.put(FROM_PROPERTY, FROM_DEFAULT);
		p.put(SMTP_HOSTNAME_PROPERTY, SMTP_HOSTNAME_DEFAULT);
		p.put(SMTP_PASSWORD_PROPERTY, SMTP_PASSWORD_DEFAULT);
		p.put(SMTP_PORT_PROPERTY, SMTP_PORT_DEFAULT);
		p.put(SMTP_USERNAME_PROPERTY, SMTP_USERNAME_DEFAULT);
		return p;
	}

}
