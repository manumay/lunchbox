package org.brainteam.lunchbox.jmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.core.Directories;
import org.springframework.beans.factory.annotation.Autowired;

abstract class PropertyConfiguration {
	
	private static final String CONFIG_COMMENT = "Lunchbox Configuration";
	
	@Autowired
	private Directories directories;
	
	private String filename;
	private Set<ConfigurationListener> listeners;
	
	protected PropertyConfiguration(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("filename may not be null");
		}
		this.filename = filename;
	}
	
	protected String getProperty(String key, String defaultValue) {
		Properties p = loadProperties();
		Object value = p.get(key);
		if (value != null) {
			return String.valueOf(value);
		}
		return defaultValue;
	}
	
	protected URL getPropertyAsURL(String key, URL defaultUrl) {
		String value = getProperty(key, defaultUrl.toExternalForm());
		try {
			return new URL(value);
		} catch (MalformedURLException e) {
			return defaultUrl;
		}
	}
	
	protected String[] getPropertyAsStringArray(String key, String defaultValue) {
		String value = getProperty(key, defaultValue);
		return StringUtils.split(value, ',');
	}
	
	protected Integer getPropertyAsInteger(String key, Integer defaultValue) {
		String value = getProperty(key, String.valueOf(defaultValue));
		if (StringUtils.isNumeric(value)) {
			return Integer.valueOf(value);
		}
		return defaultValue;
	}
	
	protected Boolean getPropertyAsBoolean(String key, Boolean defaultValue) {
		String value = getProperty(key, String.valueOf(defaultValue));
		return Boolean.valueOf(value);
	}
	
	protected void setProperty(String key, String value) {
		Properties p = loadProperties();
		p.setProperty(key, value);
		saveProperties(p);
		
	}
	
	protected Properties loadProperties() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(getConfigFile(true));
			Properties p = new Properties();
			p.load(in);
			return p;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	protected File getConfigFile(boolean createIfNotExists) {
		File file = new File(getDirectories().getConfigDir(), getFilename());
		if (createIfNotExists && !file.exists()) {
			Properties p = getDefaultProperties();
			saveProperties(p);
		}
		return file;
	}
	
	protected void saveProperties(Properties p) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getConfigFile(false));
			p.store(out, CONFIG_COMMENT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public void addConfigurationListener(ConfigurationListener listener) {
		if (listener == null) {
			return;
		}
		if (listeners == null) {
			listeners = new LinkedHashSet<>();
		}
		listeners.add(listener);
	}
	
	public void removeConfigurationListener(ConfigurationListener listener) {
		if (listener != null && listeners != null) {
			listeners.remove(listener);
		}
	}
	
	protected void notifyConfigurationListeners(String key, String value) {
		if (listeners != null && !listeners.isEmpty()) {
			for (ConfigurationListener listener : listeners) {
				listener.configurationChanged(key, value);
			}
		}
	}
	
	protected Properties getDefaultProperties() {
		return new Properties();
	}
	
	protected Directories getDirectories() {
		return directories;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
