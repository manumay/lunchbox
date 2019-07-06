package org.brainteam.lunchbox.json;

import java.util.ArrayList;
import java.util.List;

public class JsonOrdersBilling implements Comparable<JsonOrdersBilling> {

	private Integer month;
	private Integer year;
	private List<JsonOrdersMonthly> items;
	private Integer sum;
	
	public Integer getMonth() {
		return month;
	}
	
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public List<JsonOrdersMonthly> getItems() {
		if (items == null) {
			return new ArrayList<>();
		}
		return items;
	}
	
	public void addToItems(JsonOrdersMonthly item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}
	
	public Integer getSum() {
		return sum;
	}
	
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	
	@Override
	public int compareTo(JsonOrdersBilling json) {
		int year = this.getYear() - json.getYear();
		if (year != 0) {
			return year;
		}
		
		int month = this.getMonth() - json.getMonth();
		return month;
	}
	
}
