package org.brainteam.lunchbox.jmx;

public interface MailerConfiguration {
	
	String getFrom();

	String getSmtpHostName();
	
	String getSmtpPassword();
	
	Integer getSmtpPort();
	
	String getSmtpUserName();
	
}
