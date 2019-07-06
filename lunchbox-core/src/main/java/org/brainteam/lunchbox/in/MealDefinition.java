package org.brainteam.lunchbox.in;

public class MealDefinition {

	private String type;
	private String headline;
	private String description;
	private String ingredients;
	private int priceInCents;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
	
	public int getPriceInCents() {
		return priceInCents;
	}
	
	public void setPriceInCents(int priceInCents) {
		this.priceInCents = priceInCents;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result	+ ((headline == null) ? 0 : headline.hashCode());
		result = prime * result	+ ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + priceInCents;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		MealDefinition other = (MealDefinition) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		
		if (headline == null) {
			if (other.headline != null) {
				return false;
			}
		} else if (!headline.equals(other.headline)) {
			return false;
		}
		
		if (ingredients == null) {
			if (other.ingredients != null) {
				return false;
			}
		} else if (!ingredients.equals(other.ingredients)) {
			return false;
		}
		
		if (priceInCents != other.priceInCents) {
			return false;
		}
		
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return getType() + ":" + getHeadline();
	}
	
}
