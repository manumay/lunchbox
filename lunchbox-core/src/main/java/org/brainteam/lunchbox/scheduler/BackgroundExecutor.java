package org.brainteam.lunchbox.scheduler;

import java.util.List;

import org.brainteam.lunchbox.json.JsonSchedulerJob;

public interface BackgroundExecutor {

	void start();
	
	void stop();
	
	List<JsonSchedulerJob> getJobInfo();
	
}
