package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonOfferItem;
import org.brainteam.lunchbox.services.OfferItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/json/offeritem")
public class OfferItemResource {
	
	@Autowired
	private OfferItemService offerItemService;
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void put(JsonOfferItem json) {
		getOfferItemService().createNew(json);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void post(@PathVariable Long id, @RequestBody JsonOfferItem json) {
		getOfferItemService().updateExisting(json);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		getOfferItemService().deleteExisting(id);
	}
	
	protected OfferItemService getOfferItemService() {
		return offerItemService;
	}

}
