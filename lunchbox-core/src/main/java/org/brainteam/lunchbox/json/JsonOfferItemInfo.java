package org.brainteam.lunchbox.json;

public class JsonOfferItemInfo {

	private Long id;
	private String name;
	private Long mealId;
	private String headline;
	private String description;
	private String ingredients;
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
	
	public Long getMealId() {
		return mealId;
	}
	
	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getDescription() {
		return description;
	}
	 
	public void setDescription(String description) {
		this.description = description;
	}
	 
	public String getIngredients() {
		return ingredients;
	}
	 
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	public Integer getPriceInCents() {
		return priceInCents;
	}
	
	public void setPriceInCents(Integer priceInCents) {
		this.priceInCents = priceInCents;
	}
	
}
