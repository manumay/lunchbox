package org.brainteam.lunchbox.mvc;

import java.util.List;

import org.brainteam.lunchbox.json.JsonSchedulerJob;
import org.brainteam.lunchbox.scheduler.BackgroundExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/jobs")
public class SchedulerResource {
	
	@Autowired
	private BackgroundExecutor backgroundExecutor;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<JsonSchedulerJob> getJobs() {
		return getBackgroundExecutor().getJobInfo();
	}
	
	protected BackgroundExecutor getBackgroundExecutor() {
		return backgroundExecutor;
	}
	
}
