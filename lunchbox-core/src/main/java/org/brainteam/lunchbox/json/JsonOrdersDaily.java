package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonOrdersDaily {
	
	private Date date;
	private List<JsonOrdersDailyOfferItem> items;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<JsonOrdersDailyOfferItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}
	
	public void addToItems(JsonOrdersDailyOfferItem item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}
	
	public void setItems(List<JsonOrdersDailyOfferItem> items) {
		this.items = items;
	}

}
