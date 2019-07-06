package org.brainteam.lunchbox.services;

import java.net.URL;
import java.util.Date;

import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.in.OffersDefinition;
import org.brainteam.lunchbox.json.JsonOffer;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOffers;

public interface OfferService {
	
	Offer getOrCreateNew(Date date);
	
	Offer deleteExisting(Long id);

	void lockNext();
	
	void lockOlderThan(Date referenceDate);
	
	JsonOffers getNextAsJson();
	
	JsonOffer getJsonOffer(Date date);
	
	JsonOfferOrder getJsonOfferOrder(Order order);
	
	void importURL(URL url);
	
	void importDefinition(OffersDefinition offersDefinition);
	
}
