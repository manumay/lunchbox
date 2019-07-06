package org.brainteam.lunchbox.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.in.Downloader;
import org.brainteam.lunchbox.in.MealDefinition;
import org.brainteam.lunchbox.in.MenuParser;
import org.brainteam.lunchbox.in.OfferDailyDefinition;
import org.brainteam.lunchbox.in.OfferPeriodicDefinition;
import org.brainteam.lunchbox.in.OffersDefinition;
import org.brainteam.lunchbox.jmx.SystemConfiguration;
import org.brainteam.lunchbox.json.JsonOffer;
import org.brainteam.lunchbox.json.JsonOfferItemInfo;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOffers;
import org.brainteam.lunchbox.json.JsonUtils;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.DateRange;
import org.brainteam.lunchbox.util.DateUtils;
import org.brainteam.lunchbox.util.JournalUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Service
public class OfferServiceImpl implements OfferService {
	
	private static final String JOURNAL_OFFER_DELETE = "journal.offer.delete";
	private static final String JOURNAL_OFFER_LOCK = "journal.offer.lock";
	private static final String JOURNAL_OFFER_PUT = "journal.offer.put";
	
	@Autowired
	private SystemConfiguration systemConfiguration;
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private OfferItemService offerItemService;

	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private Downloader downloader;
	
	@Autowired
	private MenuParser parser;
	
	@Autowired
	private Security security;
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public Offer getOrCreateNew(Date date) {
		Date from = DateUtils.getStartOfDay(date);
		Date till = DateUtils.getEndOfDay(date);
		List<Offer> offers = getOfferRepository().findByFromAndTillOrderByDate(from, till);
		if (offers.isEmpty()) {
			Offer offer = createOffer(date);
			offer = getOfferRepository().save(offer);
			getJournalService().add(JOURNAL_OFFER_PUT, JournalUtils.getParams(offer));
			return offer;
		}
		return offers.get(0);
	}
	
	protected Offer createOffer(Date date) {
		Date offerDate = new LocalDate(date).toDateTime(getLunchTime()).toDate();
		Offer offer = new Offer();
		offer.setDate(offerDate);
		offer.setLocked(false);
		return offer;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public Offer deleteExisting(Long id) {
		if (id == null) {
			return null;
		}
		
		Offer offer = getOfferRepository().findOne(id);
		if (offer != null) {
			getOfferRepository().delete(offer);
			getJournalService().add(JOURNAL_OFFER_DELETE, JournalUtils.getParams(offer));
		}
		return null;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public void lockNext() {
		List<Offer> nextOffers = getOfferRepository().findNextOrderByDateAsc(DateUtils.now());
		if (!nextOffers.isEmpty()) {
			lockOlderThan(nextOffers.get(0).getDate());
		}
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public void lockOlderThan(Date referenceDate) {
		List<Offer> offers = getOfferRepository().findPreviousUnlockedOrderByDateDesc(referenceDate);
		for (Offer offer : offers) {
			if (offer != null && !offer.isLocked()) {
				offer.setLocked(true);
				Offer mergedOffer = getOfferRepository().save(offer);
				getJournalService().add(JOURNAL_OFFER_LOCK, JournalUtils.getParams(mergedOffer));
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('user')")
	public JsonOffers getNextAsJson() {
		List<Offer> offers = getOfferRepository().findNextOrderByDateAsc(DateUtils.now());
		Multimap<Offer,Order> orders = ArrayListMultimap.<Offer,Order>create();
		for (Offer offer : offers) {
			Date from = DateUtils.getStartOfDay(offer.getDate());
			Date till = DateUtils.getEndOfDay(offer.getDate());
			List<Order> ordersForOffer = getOrderRepository().findByFromAndTillAndUserIdOrderByDate(from, till, getSecurity().getCurrentUser().getId());
			if (!ordersForOffer.isEmpty()) {
				orders.putAll(offer, ordersForOffer);
			}
		}
		JsonOffers json = toJsonOffers(offers, orders);
		for (Offer offer : offers) {
			String offerId = String.valueOf(offer.getId());
			if (!json.getOfferOrders().containsKey(offerId)) {
				json.addToOfferOrders(offerId);
			}
		}
		return json;
	}
	
	protected JsonOffers toJsonOffers(List<Offer> offers, Multimap<Offer,Order> orders) {
		JsonOffers json = new JsonOffers();
		for (Offer offer : offers) {
			JsonOffer jsonOffer = toJsonOffer(offer.getDate(), offer);
			json.addToOffers(jsonOffer);
			
			Collection<Order> offerOrders = orders.get(offer);
			if (!offerOrders.isEmpty()) {
				JsonOfferOrder jsonOfferOrder = toJsonOfferOrder(offer, offerOrders.iterator().next());
				json.addToOfferOrders(jsonOfferOrder);
			}
		}	
		return json;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonOffer getJsonOffer(Date date) {
		Date from = DateUtils.getStartOfDay(date);
		Date till = DateUtils.getEndOfDay(date);
		List<Offer> offers = getOfferRepository().findByFromAndTillOrderByDate(from, till);
		if (offers.isEmpty()) {
			JsonOffer json = new JsonOffer();
			json.setDate(date);
			return json;
		}
		return toJsonOffer(date, offers.get(0));
	}
	
	protected JsonOffer toJsonOffer(Date date, Offer offer) {
		JsonOffer dayOffer = new JsonOffer();
		dayOffer.setDate(date);
		if (offer == null) {
			return dayOffer;
		}
		
		dayOffer.setId(offer.getId());
		for (OfferItem offerItem : offer.getItems()) {
			JsonOfferItemInfo jsonOfferItem = JsonUtils.toJsonOfferItem(offerItem);
			dayOffer.addToItems(jsonOfferItem);
		}
		dayOffer.setLocked(offer.isLocked());
		return dayOffer;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('orderer')")
	public JsonOfferOrder getJsonOfferOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("order may not be null");
		}
		return toJsonOfferOrder(order.getItem().getOffer(), order);
	}

	protected JsonOfferOrder toJsonOfferOrder(Offer offer, Order order) {
		JsonOfferOrder offerOrder = new JsonOfferOrder();
		offerOrder.setOfferId(offer.getId());
		if (order != null) {
			offerOrder.setId(order.getId());
			offerOrder.setOfferItemId(order.getItem().getId());
			offerOrder.setTimes(order.getTimes());
			offerOrder.setUserId(order.getOrderer().getId());
		}
		return offerOrder;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public void importURL(URL url) {
		if (url == null) {
			throw new IllegalArgumentException("url may not be null");
		}
		
		File menuFile = downloadFile(url);
		OffersDefinition parseResult = parseFile(menuFile);
		importDefinition(parseResult);
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public void importDefinition(OffersDefinition result) {
		for (OfferDailyDefinition daily : result.getDailyOffers()) {
			if (daily.getMeals().isEmpty()) {
				continue;
			}
			
			Date date = daily.getDate();
			Offer offer = getOrCreateNew(date);
			List<MealDefinition> mealDefinitions = daily.getMeals();
			addOfferItems(offer, mealDefinitions);
			getOfferRepository().save(offer);
		}
		
		for (OfferPeriodicDefinition periodic : result.getPeriodicOffers()) {
			List<MealDefinition> mealDefinitions = periodic.getMeals();
			if (mealDefinitions.isEmpty()) {
				continue;
			}
			
			Date startDate = periodic.getStartDate();
			Date endDate = periodic.getEndDate();
			DateRange dateRange = new DateRange(startDate, endDate);
			Iterator<Date> it = dateRange.iterator();
			
			while (it.hasNext()) {
				Date date = it.next();
				Offer offer = getOrCreateNew(date);
				addOfferItems(offer, mealDefinitions);
				getOfferRepository().save(offer);
			}
		}
	}
	
	protected void addOfferItems(Offer offer, List<MealDefinition> mealDefinitions) {
		for (MealDefinition mealDefinition : mealDefinitions) {
			OfferItem offerItem = getOfferItemService().getOrCreate(offer, mealDefinition);
			offer.addToItems(offerItem);
		}
	}
	
	protected File downloadFile(URL url) {
		try {
			return getDownloader().download(url);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}
	
	protected OffersDefinition parseFile(File file) {
		try {
			return getParser().parse(file);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}
	
	protected LocalTime getLunchTime() {
		String lunchtime = getSystemConfiguration().getLunchtime();
		return LocalTime.parse(lunchtime);
	}
	
	protected SystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}
	
	protected JournalService getJournalService() {
		return journalService;
	}
	
	protected OfferItemService getOfferItemService() {
		return offerItemService;
	}
	
	protected OfferRepository getOfferRepository() {
		return offerRepository;
	}
	
	protected OrderRepository getOrderRepository() {
		return orderRepository;
	}
	
	protected Downloader getDownloader() {
		return downloader;
	}

	protected MenuParser getParser() {
		return parser;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
}
