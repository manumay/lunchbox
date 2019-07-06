package org.brainteam.lunchbox.services;

import java.util.Date;
import java.util.List;

import org.brainteam.lunchbox.json.JsonOption;

public interface OptionService {
	
	List<JsonOption> getMeals();

	List<JsonOption> getOfferItems(Date date);
	
	List<JsonOption> getUsers();
	
}
