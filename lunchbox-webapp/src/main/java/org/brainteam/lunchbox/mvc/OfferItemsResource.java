package org.brainteam.lunchbox.mvc;

import java.util.Date;

import org.brainteam.lunchbox.json.JsonOffer;
import org.brainteam.lunchbox.services.OfferService;
import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/offeritems")
public class OfferItemsResource {
	
	@Autowired
	private OfferService offerService;
	
	@RequestMapping(value="{year}/{month}/{day}", method=RequestMethod.GET)
	public @ResponseBody JsonOffer get(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
		Date date = DateUtils.toDate(year, month, day);
		return getOfferService().getJsonOffer(date);
	}
	
	protected OfferService getOfferService() {
		return offerService;
	}

}
