package org.brainteam.lunchbox.jmx;

public interface ConfigurationProvider {

	void addConfigurationListener(ConfigurationListener listener);
	
	void removeConfigurationListener(ConfigurationListener listener);
	
}
