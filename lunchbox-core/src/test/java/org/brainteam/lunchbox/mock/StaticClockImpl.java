package org.brainteam.lunchbox.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.brainteam.lunchbox.core.Clock;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("selenium")
public class StaticClockImpl implements Clock {

	@Override
	public Date now() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse("2003-02-01");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
