package org.brainteam.lunchbox.jmx;

public interface ConfigurationListener {

	void configurationChanged(String key, String newValue);
	
}
