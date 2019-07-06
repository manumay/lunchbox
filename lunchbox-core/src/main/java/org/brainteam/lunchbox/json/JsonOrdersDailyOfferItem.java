package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.List;

public class JsonOrdersDailyOfferItem {
	
	private JsonOfferItemInfo offerItem;
	private List<JsonOrdererDetail> orderers;
	
	public JsonOfferItemInfo getOfferItem() {
		return offerItem;
	}
	
	public void setOfferItem(JsonOfferItemInfo offerItem) {
		this.offerItem = offerItem;
	}
	
	public List<JsonOrdererDetail> getOrderers() {
		return orderers;
	}
	
	public void addToOrderers(JsonOrdererDetail ordererDetail) {
		if (orderers == null) {
			orderers = new ArrayList<>();
		}
		orderers.add(ordererDetail);
	}
	
	public void setOrderers(List<JsonOrdererDetail> orderers) {
		this.orderers = orderers;
	}

}
