package org.brainteam.lunchbox.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonOption;
import org.brainteam.lunchbox.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionServiceImpl implements OptionService {
	
	@Autowired
	private MealRepository mealRepository;
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('user')")
	public List<JsonOption> getOfferItems(Date date) {
		Date from = DateUtils.getStartOfDay(date);
		Date till = DateUtils.getEndOfDay(date);
		List<Offer> offers = getOfferRepository().findByFromAndTillOrderByDate(from, till);
		if (offers.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<OfferItem> offerItems = new ArrayList<>(offers.get(0).getItems());
		Collections.sort(offerItems);
		
		List<JsonOption> json = new ArrayList<>();
		for (OfferItem offerItem : offerItems) {
			JsonOption option = new JsonOption();
			option.setValue(offerItem.getId());
			option.setText(offerItem.getName() + ": " + offerItem.getMeal().getHeadline());
			json.add(option);
		}
		return json;
	}

	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public List<JsonOption> getUsers() {
		List<JsonOption> json = new ArrayList<>();
		List<User> activeUsers = getUserRepository().findByActiveTrue();
		Collections.sort(activeUsers);
		for (User user : activeUsers) {
			JsonOption option = new JsonOption();
			option.setValue(user.getId());
			option.setText(user.getFullName());
			json.add(option);
		}
		return json;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public List<JsonOption> getMeals() {
		List<JsonOption> json = new ArrayList<>();
		List<Meal> meals = getMealRepository().findAll();
		Collections.sort(meals);
		for (Meal meal : meals) {
			JsonOption option = new JsonOption();
			option.setValue(meal.getId());
			option.setText(meal.getHeadline() + " " + meal.getDescription());
			json.add(option);
		}
		return json;
	}
	
	protected MealRepository getMealRepository() {
		return mealRepository;
	}
	
	protected OfferRepository getOfferRepository() {
		return offerRepository;
	}
	
	protected UserRepository getUserRepository() {
		return userRepository;
	}
	
}
