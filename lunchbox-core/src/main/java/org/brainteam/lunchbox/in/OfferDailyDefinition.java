package org.brainteam.lunchbox.in;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferDailyDefinition {

	private Date date;
	private List<MealDefinition> meals = new ArrayList<>();
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<MealDefinition> getMeals() {
		return meals;
	}
	
	public void addMeal(MealDefinition meal) {
		if (!meals.contains(meal)) {
			meals.add(meal);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((meals == null) ? 0 : meals.hashCode());
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
		
		OfferDailyDefinition other = (OfferDailyDefinition) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		
		if (meals == null) {
			if (other.meals != null) {
				return false;
			}
		} else if (!meals.equals(other.meals)) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return new SimpleDateFormat("dd-MM-yyyy").format(getDate()) + ": " + meals.toString();
	}
	
}
