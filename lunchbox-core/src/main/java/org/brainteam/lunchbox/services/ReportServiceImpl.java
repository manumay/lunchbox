package org.brainteam.lunchbox.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.jmx.SystemConfiguration;
import org.brainteam.lunchbox.json.JsonOfferItemInfo;
import org.brainteam.lunchbox.json.JsonOrdererDetail;
import org.brainteam.lunchbox.json.JsonOrdersBilling;
import org.brainteam.lunchbox.json.JsonOrdersDaily;
import org.brainteam.lunchbox.json.JsonOrdersDailyOfferItem;
import org.brainteam.lunchbox.json.JsonOrdersDayOfMonth;
import org.brainteam.lunchbox.json.JsonOrdersMonthly;
import org.brainteam.lunchbox.json.JsonOrdersMonthlyOrderItem;
import org.brainteam.lunchbox.json.JsonUtils;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.DateRange;
import org.brainteam.lunchbox.util.DateUtils;
import org.brainteam.lunchbox.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private SystemConfiguration systemConfiguration;
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private Security security;

	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('user')")
	public JsonOrdersDaily getDailyReportJson(Date day) {
		Date from = DateUtils.getStartOfDay(day);
		Date till = DateUtils.getEndOfDay(day);
		
		List<Offer> offers = getOfferRepository().findByFromAndTillOrderByDate(from, till);
		if (offers.isEmpty()) {
			JsonOrdersDaily daily = new JsonOrdersDaily();
			daily.setDate(day);
			return daily;
		}
		
		Date nextDate = offers.get(0).getDate();
		List<Order> orders = getOrderRepository().findByFromAndTillOrderByDate(from, till);
		return toJsonOrdersDaily(nextDate, orders);
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('user')")
	public JsonOrdersDaily getDailyReportJson(Offer offer) {
		List<Order> orders = getOrderRepository().findByOfferId(offer.getId());
		return toJsonOrdersDaily(offer.getDate(), orders);
	}
	
	protected JsonOrdersDaily toJsonOrdersDaily(Date date, List<Order> orders) {
		JsonOrdersDaily json = new JsonOrdersDaily();
		json.setDate(date);
		
		List<OfferItem> items = new ArrayList<>();
		for (Order order : orders) {
			OfferItem item = order.getItem();
			if (!items.contains(item)) {
				items.add(item);
			}
		}
		Collections.sort(items);
		
		Multimap<OfferItem, Pair<User,Integer>> data = ArrayListMultimap.<OfferItem,Pair<User,Integer>>create();
		for (OfferItem item : items) {
			for (Order order : orders) {
				if (order.getItem().equals(item)) {
					data.put(item, new Pair<>(order.getOrderer(), order.getTimes()));
				}
			}
		}
		
		Map<OfferItem,Collection<Pair<User,Integer>>> dataMap = data.asMap();
		List<OfferItem> sortedOfferItems = new ArrayList<>(dataMap.keySet());
		Collections.sort(sortedOfferItems);
		for (OfferItem item : sortedOfferItems) {
			Collection<Pair<User,Integer>> orderers = dataMap.get(item);
			JsonOrdersDailyOfferItem jsonItem = toJsonOrdersDailyOfferItem(item, orderers);
			json.addToItems(jsonItem);
		}
		return json;
	}
	
	protected JsonOrdersDailyOfferItem toJsonOrdersDailyOfferItem(OfferItem item, Collection<Pair<User,Integer>> orders) {
		JsonOrdersDailyOfferItem json = new JsonOrdersDailyOfferItem();
		JsonOfferItemInfo jsonItem = JsonUtils.toJsonOfferItem(item);
		json.setOfferItem(jsonItem);

		List<Pair<User,Integer>> sortedOrders = new ArrayList<>(orders);
		Collections.sort(sortedOrders);
		for (Pair<User,Integer> pair : sortedOrders) {
			JsonOrdererDetail ordererDetail = new JsonOrdererDetail();
			ordererDetail.setUserId(pair.getFirst().getId());
			ordererDetail.setUserName(pair.getFirst().getFullName());
			ordererDetail.setTimes(pair.getSecond());
			json.addToOrderers(ordererDetail);
		}
		
		return json;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('user')")
	public JsonOrdersMonthly getMonthlyReportJson(Integer month, Integer year) {
		Date from = DateUtils.getFirstDayOfMonth(month, year);
		Date till = DateUtils.getEndOfMonth(month, year);
		Long currentUserId = getSecurity().getCurrentUser().getId();
		List<Order> ordersForMonth = getOrderRepository().findByFromAndTillAndLockedAndUserIdOrderByDate(from, till, true, currentUserId);
		return toJsonOrdersMonthly(year, month, getSecurity().getCurrentUser(), ordersForMonth);
	}
	
	protected JsonOrdersMonthly toJsonOrdersMonthly(Integer year, Integer month, User user, List<Order> orders) {
		JsonOrdersMonthly json = new JsonOrdersMonthly();
		if (getSecurity().isCurrentUserAdmin()) {
			json.setPersonnelNumber(user.getPersonnelNumber());
		}
		json.setUserFullname(user.getFullName());
		json.setMonth(month);
		json.setYear(year);
		
		Multimap<Date, Order> datedOrders = ArrayListMultimap.<Date,Order>create(); 
		for (Order order : orders) {
			Date startOfDay = DateUtils.getStartOfDay(order.getItem().getOffer().getDate());
			datedOrders.put(startOfDay, order);
		}
		
		long sum = 0;
		Date startDate = DateUtils.getFirstDayOfMonth(month, year);
		Date endDate = DateUtils.getLastDayOfMonth(month, year);
		DateRange range = new DateRange(startDate, endDate);
		Iterator<Date> it = range.iterator();
		while (it.hasNext()) {
			Date date = it.next();
			if (getSystemConfiguration().isWeekdaysOnly() && !DateUtils.isWeekday(date)) {
				continue;
			}
			JsonOrdersDayOfMonth day = toJsonOrdersDayOfMonth(date, datedOrders);
			json.addToDays(day);
			sum += day.getSum();
		}
		json.setSum(sum);
		
		return json;
	}
	
	protected JsonOrdersDayOfMonth toJsonOrdersDayOfMonth(Date date, Multimap<Date, Order> datedOrders) {
		JsonOrdersDayOfMonth json = new JsonOrdersDayOfMonth();
		json.setDate(date);
		long sum = 0;
		if (datedOrders.containsKey(date)) {
			List<Order> ordersForDate = new ArrayList<>(datedOrders.get(date));
			Collections.sort(ordersForDate);
			for (Order order : ordersForDate) {
				JsonOrdersMonthlyOrderItem item = new JsonOrdersMonthlyOrderItem();
				item.setOfferItem(JsonUtils.toJsonOfferItem(order.getItem()));
				item.setTimes(order.getTimes());
				item.setPriceInCents(Long.valueOf(order.getTimes().longValue() * order.getItem().getPriceInCents().longValue()));
				json.addToItems(item);
				sum += item.getPriceInCents();
			}
		}
		json.setSum(sum);
		return json;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public List<JsonOrdersBilling> getBillingReportJson(Integer month, Integer year, int count) {
		List<JsonOrdersBilling> jsonBillings = new ArrayList<>();
		int currentMonth = month;
		int currentYear = year;
		for (int i = 0; i < count; i++) {
			if (currentMonth == 0) {
				currentMonth = 12;
				currentYear -= 1;
			}
			Date from = DateUtils.getFirstDayOfMonth(currentMonth, currentYear);
			Date till = DateUtils.getEndOfMonth(currentMonth, currentYear);
			List<Order> ordersForMonth = getOrderRepository().findByFromAndTillAndLockedOrderByDate(from, till, true);
			JsonOrdersBilling jsonBilling = toJsonOrdersBilling(currentYear, currentMonth, ordersForMonth);
			jsonBillings.add(jsonBilling);
			currentMonth--;
		}
		return jsonBillings;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonOrdersBilling getBillingReportJson(Integer month, Integer year) {
		List<JsonOrdersBilling> json = getBillingReportJson(month, year, 1);
		return !json.isEmpty() ? json.get(0) : null;
	}
	
	protected JsonOrdersBilling toJsonOrdersBilling(Integer year, Integer month, List<Order> orders) {
		List<User> orderers = new ArrayList<>();
		Multimap<User,Order> usersOrders = ArrayListMultimap.<User,Order>create(); 
		for (Order order : orders) {
			User orderer = order.getOrderer();
			if (!orderers.contains(orderer)) {
				orderers.add(order.getOrderer());
			}
			usersOrders.put(orderer, order);
		}
		Collections.sort(orderers);
		
		JsonOrdersBilling json = new JsonOrdersBilling();
		json.setMonth(month);
		json.setYear(year);
		int sum = 0;
		for (User orderer : orderers) {
			List<Order> userOrders = new ArrayList<>(usersOrders.get(orderer));
			JsonOrdersMonthly item = toJsonOrdersMonthly(year, month, orderer, userOrders);
			json.addToItems(item);
			sum += item.getSum();
		}
		json.setSum(sum);
		
		return json;
	}
	
	protected SystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}
	
	protected OfferRepository getOfferRepository() {
		return offerRepository;
	}
	
	protected OrderRepository getOrderRepository() {
		return orderRepository;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
}
