package org.brainteam.lunchbox.services;

import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferItemRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.in.MealDefinition;
import org.brainteam.lunchbox.json.JsonMeal;
import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.util.JournalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MealServiceImpl implements MealService {
	
	private static final String JOURNAL_MEAL_DELETE = "journal.meal.delete";
	private static final String JOURNAL_MEAL_POST = "journal.meal.post";
	private static final String JOURNAL_MEAL_PUT = "journal.meal.put";
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private MealRepository mealRepository;
	
	@Autowired
	private OfferItemRepository offerItemRepository;
	
	@Override
	public Meal createNew(JsonMeal jsonMeal) {
		Meal meal = fromJson(jsonMeal);
		return createNew(meal);
	}
	
	protected Meal createNew(Meal meal) {
		Meal createdMeal = getMealRepository().save(meal);
		getJournalService().add(JOURNAL_MEAL_PUT, JournalUtils.getParams(createdMeal));
		return createdMeal;
	}
	
	@Override
	public Meal updateExisting(JsonMeal jsonMeal) {
		Meal existingMeal = fromJson(jsonMeal);
		if (existingMeal != null) {
			Meal updatedMeal = getMealRepository().save(existingMeal);
			getJournalService().add(JOURNAL_MEAL_POST, JournalUtils.getParams(updatedMeal));
			return updatedMeal;
		}
		return null;
	}
	
	@Override
	public Meal deleteExisting(Long mealId) {
		if (mealId == null) {
			return null;
		}
		
		Meal meal = getMealRepository().findOne(mealId);
		if (meal != null) {
			getMealRepository().delete(meal);
			getJournalService().add(JOURNAL_MEAL_DELETE, JournalUtils.getParams(meal));
			return meal;
		}
		return null;
	}

	@Override
	@Transactional
	public Meal importDefinition(MealDefinition mealDefinition) {
		String headline = mealDefinition.getHeadline();
		String description = mealDefinition.getDescription();
		String ingredients = mealDefinition.getIngredients();
		
		Meal meal = getMealRepository().findByHeadlineAndDescriptionAndIngredients(headline, description, ingredients);
		if (meal == null) {
			meal = createInstance(mealDefinition);
			createNew(meal);
			return meal;
		}
		return meal;
	}
	
	protected Meal createInstance(MealDefinition mealDefinition) {
		return createInstance(mealDefinition.getHeadline(), mealDefinition.getDescription(), mealDefinition.getIngredients());
	}
	
	protected Meal createInstance(String headline, String description, String ingredients) {
		Meal meal = new Meal();
		meal.setHeadline(headline);
		meal.setDescription(description);
		meal.setIngredients(ingredients);
		return meal;
	}
	
	@Override
	public JsonPage<JsonMeal> getJson(int page, int size, String sort, String order, String search) {
		JsonPage<JsonMeal> json = new JsonPage<>();
		PageRequest pr = new PageRequest(page, size, new Sort(new Order(Direction.fromString(order), sort)));
		boolean doFilter = !StringUtils.isEmpty(search);
		Page<Meal> meals = doFilter ? searchMealByHeadline(search, pr) : getMealRepository().findAll(pr);
		for (Meal meal : meals) {
			json.addToItems(toJson(meal));
		}
		json.setPage(meals.getNumber() + 1);
		json.setSize(meals.getSize());
		json.setNumberOfItems(meals.getNumberOfElements());
		json.setSort(sort);
		json.setOrder(order);
		json.setTotalPages(meals.getTotalPages());
		json.setTotalItems(meals.getTotalElements());
		return json;
	}
	
	protected Page<Meal> searchMealByHeadline(String searchString, PageRequest pr) {
		String search = StringUtils.removeStart(searchString, "%");
		search = StringUtils.removeEnd(search, "%");
		search = "%" + search + "%";
		return getMealRepository().findByHeadlineLikeOrDescriptionLike(search, search, pr);
	}
	
	@Override
	@Transactional(readOnly=true)
	public JsonMeal toJson(Meal meal) {
		JsonMeal json = new JsonMeal();
		json.setId(meal.getId());
		json.setHeadline(meal.getHeadline());
		json.setDescription(meal.getDescription());
		json.setIngredients(meal.getIngredients());
		json.setOfferItemsCount(getOfferItemRepository().findOfferItemCount(meal.getId()));
		return json;
	}
	
	protected Meal fromJson(JsonMeal json) {
		if (json == null) {
			throw new IllegalArgumentException("json may not be null");
		}
		
		Meal meal = getOrCreate(json.getId());
		meal.setHeadline(StringUtils.trim(json.getHeadline()));
		meal.setDescription(StringUtils.trim(json.getDescription()));
		meal.setIngredients(StringUtils.trim(json.getIngredients()));
		return meal;
	}
	
	protected Meal getOrCreate(Long id) {
		if (id == null) {
			return new Meal();
		}
		
		Meal existingMeal = getMealRepository().findOne(id);
		if (existingMeal != null) {
			return existingMeal;
		}
		
		return new Meal();
	}
	
	protected JournalService getJournalService() {
		return journalService;
	}
	
	protected MealRepository getMealRepository() {
		return mealRepository;
	}
	
	protected OfferItemRepository getOfferItemRepository() {
		return offerItemRepository;
	}
	
}
