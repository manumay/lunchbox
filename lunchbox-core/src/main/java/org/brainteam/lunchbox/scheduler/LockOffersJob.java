package org.brainteam.lunchbox.scheduler;

import org.brainteam.lunchbox.cmd.LockOffersCommand;

public class LockOffersJob extends BackgroundActionJob {

	public LockOffersJob() {
		super(LockOffersCommand.class);
	}
	
}
