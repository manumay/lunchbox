package org.brainteam.lunchbox.json;

import java.util.Date;

public class JsonOfferItem {
	
	private Long id;
	private String name;
	private Date offerDate;
	private Long mealId;
	private Integer priceInCents;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getOfferDate() {
		return offerDate;
	}
	
	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}
	
	public Long getMealId() {
		return mealId;
	}
	
	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}
	
	public Integer getPriceInCents() {
		return priceInCents;
	}
	
	public void setPriceInCents(Integer priceInCents) {
		this.priceInCents = priceInCents;
	}

}
