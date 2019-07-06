package org.brainteam.lunchbox.core;

import java.util.Date;

import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class ClockImpl implements Clock {

	@Override
	public Date now() {
		return DateUtils.now();
	}
	
}
