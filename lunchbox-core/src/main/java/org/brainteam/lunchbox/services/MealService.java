package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.in.MealDefinition;
import org.brainteam.lunchbox.json.JsonMeal;
import org.brainteam.lunchbox.json.JsonPage;

public interface MealService {
	
	Meal importDefinition(MealDefinition mealDefinition);
	
	Meal createNew(JsonMeal json);
	
	Meal updateExisting(JsonMeal json);
	
	Meal deleteExisting(Long mealId);
	
	JsonMeal toJson(Meal meal);
	
	JsonPage<JsonMeal> getJson(int page, int size, String sort, String order, String search);
	
}
