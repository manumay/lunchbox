package org.brainteam.lunchbox.i18n;

public interface Translator {
	
	String getApplicationName();
	
	String t(String key);

	String t(String key, Object ... arguments);
	
}
