package org.brainteam.lunchbox.mvc;

import java.util.Date;

import org.brainteam.lunchbox.json.JsonJournal;
import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.services.JournalService;
import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/journal")
public class JournalResource {
	
	@Autowired
	private JournalService journalService;

	@RequestMapping(value="{year}/{month}/{day}", method = RequestMethod.GET)
	public @ResponseBody JsonPage<JsonJournal> getForDay(@PathVariable Integer year, @PathVariable Integer month, 
			@PathVariable Integer day, @RequestParam int page, @RequestParam int size, @RequestParam String sort, @RequestParam String order) {
		Date date = DateUtils.toDate(year, month, day);
		return getJournalService().getJson(page - 1, size, sort, order, date);
	}
	
	protected JournalService getJournalService() {
		return journalService;
	}
	
}
