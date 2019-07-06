package org.brainteam.lunchbox.in;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferPeriodicDefinition {

	private Date startDate;
	private Date endDate;
	private List<MealDefinition> meals = new ArrayList<>();
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((meals == null) ? 0 : meals.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		
		OfferPeriodicDefinition other = (OfferPeriodicDefinition) obj;
		if (endDate == null) {
			if (other.endDate != null) {
				return false;
			}
		} else if (!endDate.equals(other.endDate)) {
			return false;
		}
		
		if (meals == null) {
			if (other.meals != null) {
				return false;
			}
		} else if (!meals.equals(other.meals)) {
			return false;
		}
		
		if (startDate == null) {
			if (other.startDate != null) {
				return false;
			}
		} else if (!startDate.equals(other.startDate)) {
			return false;
		}
		
		return true;
	}
	
	
	
}
