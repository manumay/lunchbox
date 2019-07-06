package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonOffers {

	private List<JsonOffer> offers;
	private Map<String, JsonOfferOrder> offerOrders;
	
	public List<JsonOffer> getOffers() {
		return offers;
	}
	
	public void addToOffers(JsonOffer offer) {
		if (offers == null) {
			offers = new ArrayList<>();
		}
		offers.add(offer);
	}
	
	public Map<String, JsonOfferOrder> getOfferOrders() {
		if (offerOrders == null) {
			offerOrders = new HashMap<>();
		}
		return offerOrders;
	}
	
	public void addToOfferOrders(String offerId) {
		if (offerOrders == null) {
			offerOrders = new HashMap<>();
		}
		offerOrders.put(offerId, new JsonOfferOrder());
	}
	
	public void addToOfferOrders(JsonOfferOrder order) {
		if (offerOrders == null) {
			offerOrders = new HashMap<>();
		}
		offerOrders.put(String.valueOf(order.getOfferId()), order);
	}
	
}
