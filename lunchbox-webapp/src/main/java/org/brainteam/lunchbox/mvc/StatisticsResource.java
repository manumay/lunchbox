package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonSystemStatistics;
import org.brainteam.lunchbox.json.JsonUserStatistics;
import org.brainteam.lunchbox.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/statistics")
public class StatisticsResource {
	
	@Autowired
	private StatisticService statisticsService;

	@RequestMapping(value="me", method=RequestMethod.GET)
	public @ResponseBody JsonUserStatistics getCurrentUserStatistics() {
		return getStatisticsService().getCurrentUserStatistics();
	}
	
	@RequestMapping(value="user/{id}", method=RequestMethod.GET)
	public @ResponseBody JsonUserStatistics getUserStatistics(@PathVariable Long id) {
		return getStatisticsService().getUserStatistics(id);
	}
	
	@RequestMapping(value="system", method=RequestMethod.GET)
	public @ResponseBody JsonSystemStatistics getSystemStatistics() {
		return getStatisticsService().getSystemStatistics();
	}
	
	protected StatisticService getStatisticsService() {
		return statisticsService;
	}
	
}
