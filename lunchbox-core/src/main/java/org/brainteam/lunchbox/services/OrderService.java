package org.brainteam.lunchbox.services;

import java.util.Date;

import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOrder;
import org.brainteam.lunchbox.json.JsonOrders;

public interface OrderService {
	
	Order createNew(JsonOfferOrder jsonOrder);
	
	void updateExisting(JsonOfferOrder jsonOrder);

	void deleteExisting(Long id);
	
	JsonOrders getJsonOrders(Date date);
	
	JsonOrder getJsonOrder(Long id);
	
}
