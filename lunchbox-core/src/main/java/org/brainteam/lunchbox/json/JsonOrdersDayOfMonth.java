package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonOrdersDayOfMonth {

	private Date date;
	private List<JsonOrdersMonthlyOrderItem> items;
	private Long sum;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<JsonOrdersMonthlyOrderItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}
	
	public void addToItems(JsonOrdersMonthlyOrderItem item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}

	public Long getSum() {
		return sum;
	}
	
	public void setSum(Long sum) {
		this.sum = sum;
	}
	
}
