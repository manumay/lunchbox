package org.brainteam.lunchbox.json;

import org.brainteam.lunchbox.domain.OfferItem;

public final class JsonUtils {

	private JsonUtils() {
	}
	
	public static JsonOfferItemInfo toJsonOfferItem(OfferItem offerItem) {
		JsonOfferItemInfo json = new JsonOfferItemInfo();
		json.setDescription(offerItem.getMeal().getDescription());
		json.setHeadline(offerItem.getMeal().getHeadline());
		json.setId(offerItem.getId());
		json.setIngredients(offerItem.getMeal().getIngredients());
		json.setMealId(offerItem.getMeal().getId());
		json.setName(offerItem.getName());
		json.setPriceInCents(offerItem.getPriceInCents());
		return json;
	}
	
}
