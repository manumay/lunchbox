package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonOffer {

	private Long id;
	private Date date;
	private List<JsonOfferItemInfo> items;
	private boolean locked;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<JsonOfferItemInfo> getItems() {
		return items;
	}
	
	public void addToItems(JsonOfferItemInfo item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
