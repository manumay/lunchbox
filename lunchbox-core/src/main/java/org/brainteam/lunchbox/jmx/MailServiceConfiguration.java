package org.brainteam.lunchbox.jmx;

public interface MailServiceConfiguration {
	
	String getMailBillingSubject();
	
	String getMailBillingInfo();
	
	String[] getMailBillingToAsArray();
	
	String[] getMailBillingCCAsArray();
	
	String[] getMailBillingBCCAsArray();
	
	String getMailOrdersSubject();
	
	String getMailOrdersInfo();
	
	String[] getMailOrdersToAsArray();
	
	String[] getMailOrdersCCAsArray();
	
	String[] getMailOrdersBCCAsArray();

}
