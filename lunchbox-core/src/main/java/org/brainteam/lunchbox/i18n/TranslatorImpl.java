package org.brainteam.lunchbox.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.brainteam.lunchbox.jmx.InternationalizationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslatorImpl implements Translator {
	
	static final String RESOURCEBUNDLE_NAME = "org.brainteam.lunchbox.i18n.messages";
	private static final String KEY_APPLICATION_NAME = "application.name";
	
	@Autowired
	private InternationalizationConfiguration configuration;

	private Map<Locale,ResourceBundle> bundles = new HashMap<>();
	
	@Override
	public String getApplicationName() {
		return t(KEY_APPLICATION_NAME);
	}
	
	@Override
	public String t(String key) {
		if (key == null || key.isEmpty()) {
			return "?";
		}
		
		try {
            return getBundle().getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
	}
	
	@Override
	public String t(String key, Object... arguments) {
		if (key == null || key.isEmpty()) {
			return "?";
		}
		
		try {
            return MessageFormat.format(getBundle().getString(key), arguments);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
	}
	
	public ResourceBundle getBundle() {
		return getBundle(getConfiguration().getLocale());
	}
	
	public ResourceBundle getBundle(Locale locale) {
		if (!bundles.containsKey(locale)) {
			ResourceBundle bundle = ResourceBundle.getBundle(RESOURCEBUNDLE_NAME, locale);
			bundles.put(locale, bundle);
		}
		return bundles.get(locale);
	}
	
	protected InternationalizationConfiguration getConfiguration() {
		return configuration;
	}

}
