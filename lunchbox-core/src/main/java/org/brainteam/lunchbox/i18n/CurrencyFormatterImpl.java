package org.brainteam.lunchbox.i18n;

import java.text.NumberFormat;

import org.brainteam.lunchbox.jmx.InternationalizationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyFormatterImpl implements CurrencyFormatter {

	@Autowired
	private InternationalizationConfiguration configuration;
	
	@Override
	public String format(Long priceInCents) {
		Long valueToConvert = priceInCents;
		if (valueToConvert == null) {
			valueToConvert = Long.valueOf(0);
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance(getConfiguration().getLocale());
		return formatter.format(valueToConvert / 100.0);
	}
	
	public static String toString(Long number) {
		return number != null ? String.valueOf(number) : "0";
	}
	
	protected InternationalizationConfiguration getConfiguration() {
		return configuration;
	}
}
