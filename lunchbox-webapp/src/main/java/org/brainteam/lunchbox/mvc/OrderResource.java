package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOrder;
import org.brainteam.lunchbox.services.OfferService;
import org.brainteam.lunchbox.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/json/order")
public class OrderResource {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OfferService offerService;
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public @ResponseBody JsonOrder get(@PathVariable Long id) {
		return getOrderService().getJsonOrder(id);
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public @ResponseBody JsonOfferOrder put(@RequestBody JsonOfferOrder json) {
		Order newOrder = getOrderService().createNew(json);
		return getOfferService().getJsonOfferOrder(newOrder);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void post(@PathVariable Long id, @RequestBody JsonOfferOrder json) {
		getOrderService().updateExisting(json);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		getOrderService().deleteExisting(id);
	}
	
	protected OrderService getOrderService() {
		return orderService;
	}
	
	protected OfferService getOfferService() {
		return offerService;
	}

}
