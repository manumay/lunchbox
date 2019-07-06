package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.in.MealDefinition;
import org.brainteam.lunchbox.json.JsonOfferItem;

public interface OfferItemService {

	OfferItem getOrCreate(Offer offer, MealDefinition mealDefinition);
	
	OfferItem createNew(JsonOfferItem json);
	
	OfferItem updateExisting(JsonOfferItem json);
	
	OfferItem deleteExisting(Long id);
	
}
