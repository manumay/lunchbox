package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferItemRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.in.MealDefinition;
import org.brainteam.lunchbox.json.JsonOfferItem;
import org.brainteam.lunchbox.util.JournalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfferItemServiceImpl implements OfferItemService {
	
	private static final String JOURNAL_OFFERITEM_POST = "journal.offeritem.post";
	private static final String JOURNAL_OFFERITEM_PUT = "journal.offeritem.put";
	private static final String JOURNAL_OFFERITEM_DELETE = "journal.offeritem.delete";
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private MealService mealService;
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private MealRepository mealRepository;
	
	@Autowired
	private OfferItemRepository offerItemRepository;

	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public OfferItem getOrCreate(Offer offer, MealDefinition mealDefinition) {
		Meal meal = getMealService().importDefinition(mealDefinition);
		OfferItem offerItem = get(offer, meal);
		if (offerItem == null) {
			offerItem = createOfferItem(offer, meal, mealDefinition);
			offerItem = getOfferItemRepository().saveAndFlush(offerItem);
			getJournalService().add(JOURNAL_OFFERITEM_PUT, JournalUtils.getParams(offerItem));
		}
		return offerItem;
	}
	
	protected OfferItem createOfferItem(Offer offer, Meal meal, MealDefinition mealDefinition) {
		OfferItem offerItem = new OfferItem();
		offerItem.setMeal(meal);
		offerItem.setName(mealDefinition.getType());
		offerItem.setOffer(offer);
		offerItem.setPriceInCents(mealDefinition.getPriceInCents());
		return offerItem;
	}
	
	public OfferItem get(Offer offer, Meal meal) {
		return null;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public OfferItem createNew(JsonOfferItem json) {
		OfferItem offerItem = fromJson(json);
		offerItem = getOfferItemRepository().save(offerItem);
		getJournalService().add(JOURNAL_OFFERITEM_PUT, JournalUtils.getParams(offerItem));
		return offerItem;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public OfferItem updateExisting(JsonOfferItem json) {
		OfferItem offerItem = fromJson(json);
		offerItem = getOfferItemRepository().save(offerItem);
		getJournalService().add(JOURNAL_OFFERITEM_POST, JournalUtils.getParams(offerItem));
		return offerItem;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public OfferItem deleteExisting(Long offerItemId) {
		if (offerItemId == null) {
			return null;
		}
		
		OfferItem offerItem = getOfferItemRepository().findOne(offerItemId);
		if (shouldDelete(offerItem)) {
			Offer offer = offerItem.getOffer();
			getOfferItemRepository().delete(offerItem);
			offer.removeFromItems(offerItem);
			getJournalService().add(JOURNAL_OFFERITEM_DELETE, JournalUtils.getParams(offerItem));
			if (offer.getItems().isEmpty()) {
				getOfferService().deleteExisting(offer.getId());
			}
			return offerItem;
		}
		
		return null;
	}
	
	protected boolean shouldDelete(OfferItem offerItem) {
		return offerItem != null;
	}
	
	private OfferItem fromJson(JsonOfferItem json) {
		if (json == null) {
			throw new IllegalArgumentException("json may not be null");
		}
		
		OfferItem offerItem = getOrCreate(json.getId());
		offerItem.setName(json.getName());
		offerItem.setOffer(getOfferService().getOrCreateNew(json.getOfferDate()));
		offerItem.setMeal(getMealRepository().findOne(json.getMealId()));
		offerItem.setPriceInCents(json.getPriceInCents());
		return offerItem;
	}
	
	private OfferItem getOrCreate(Long id) {
		if (id != null) {
			OfferItem existingOfferItem = getOfferItemRepository().findOne(id);
			if (existingOfferItem != null) {
				return existingOfferItem;
			}
		}
		return new OfferItem();
	}

	protected JournalService getJournalService() {
		return journalService;
	}
	
	protected MealService getMealService() {
		return mealService;
	}
	
	protected OfferService getOfferService() {
		return offerService;
	}
	
	protected MealRepository getMealRepository() {
		return mealRepository;
	}

	protected OfferItemRepository getOfferItemRepository() {
		return offerItemRepository;
	}

}
