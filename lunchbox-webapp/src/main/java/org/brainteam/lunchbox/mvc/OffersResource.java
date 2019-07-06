package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonOffers;
import org.brainteam.lunchbox.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/offers")
public class OffersResource {
	
	@Autowired
	private OfferService offerService;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody JsonOffers get() {
		return getOfferService().getNextAsJson();
	}
	
	protected OfferService getOfferService() {
		return offerService;
	}
	
}
