package org.brainteam.lunchbox.services;

import java.util.Date;
import java.util.List;

import org.brainteam.lunchbox.core.Clock;
import org.brainteam.lunchbox.core.LunchboxRuntimeException;
import org.brainteam.lunchbox.dao.OfferItemRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonOffer;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOrder;
import org.brainteam.lunchbox.json.JsonOrdererDetail;
import org.brainteam.lunchbox.json.JsonOrders;
import org.brainteam.lunchbox.json.JsonUtils;
import org.brainteam.lunchbox.security.OrderSecurity;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.DateUtils;
import org.brainteam.lunchbox.util.JournalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	static final String JOURNAL_ORDER_DELETE = "journal.order.delete";
	static final String JOURNAL_ORDER_POST = "journal.order.post";
	static final String JOURNAL_ORDER_PUT = "journal.order.put";
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OfferItemRepository offerItemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Security security;
	
	@Autowired
	private OrderSecurity orderSecurity;
	
	@Autowired
	private Clock clock;
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('" + Security.ROLE_USER + "')")
	public Order createNew(JsonOfferOrder jsonOrder) {
		if (jsonOrder == null) {
			throw new IllegalArgumentException("jsonOrder may not be null");
		}

		if (jsonOrder.getOfferItemId() == null) {
			throw new LunchboxRuntimeException("missing offerItemId");
		}
		
		Order mergedOrder = mergeOrder(jsonOrder, true);
		if (mergedOrder != null) {
			getJournalService().add(JOURNAL_ORDER_PUT, JournalUtils.getParams(mergedOrder));
		}
		return mergedOrder;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('" + Security.ROLE_USER + "')")
	public void updateExisting(JsonOfferOrder jsonOrder) {
		if (jsonOrder == null) {
			throw new IllegalArgumentException("jsonOrder may not be null");
		}
		
		if (jsonOrder.getId() == null) {
			throw new LunchboxRuntimeException("missing id");
		}
		
		if (jsonOrder.getOfferItemId() == null) {
			throw new LunchboxRuntimeException("missing offerItemId");
		}
		
		Order mergedOrder = mergeOrder(jsonOrder, false);
		if (mergedOrder != null) {
			getJournalService().add(JOURNAL_ORDER_POST, JournalUtils.getParams(mergedOrder));
		}
	}
	
	protected Order mergeOrder(JsonOfferOrder jsonOrder, boolean insert) {
		if (shouldMerge(jsonOrder)) {
			Order order = getOrCreateOrder(jsonOrder);
			getOrderSecurity().checkPermission(OrderSecurity.MERGE, order);
			return getOrderRepository().save(order);
		}
		return null;
	}
	
	protected boolean shouldMerge(JsonOfferOrder jsonOrder) {
		OfferItem offerItem = getOfferItemRepository().findOne(jsonOrder.getOfferItemId());
		return offerItem != null;
	}
	
	protected Order getOrCreateOrder(JsonOfferOrder offerOrder) {
		Order order = getBestMatchingOrder(offerOrder);
		order.setOrderer(getUser(offerOrder));
		order.setItem(getOfferItem(offerOrder));
		order.setOrderedDate(getClock().now());
		order.setTimes(getTimes(offerOrder));
		return order;
	}
	
	protected Order getBestMatchingOrder(JsonOfferOrder offerOrder) {
		// Finde über die Id
		Long orderId = offerOrder.getId();
		if (orderId != null) {
			Order idOrder = getOrderRepository().findOne(orderId);
			if (idOrder != null) {
				return idOrder;
			}
		}
		return new Order();
	}
	
	protected User getUser(JsonOfferOrder offerOrder) {
		Long userId = offerOrder.getUserId();
		if (userId == null || getSecurity().isCurrentUserNotAdmin()) {
			return getSecurity().getCurrentUser();
		} else {
			return getUserRepository().findOne(userId);
		}
	}
	
	protected OfferItem getOfferItem(JsonOfferOrder offerOrder) {
		Long offerItemId = offerOrder.getOfferItemId();
		return offerItemId == null ? null : getOfferItemRepository().findOne(offerItemId);
	}
	
	protected Integer getTimes(JsonOfferOrder offerOrder) {
		Integer times =  offerOrder.getTimes();
		if (times != null && times >= 1) {
			return times;
		}
		return 1;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('user')")
	public void deleteExisting(Long orderId) {
		Order order = getOrderRepository().findOne(orderId);
		if (shouldDelete(order)) {
			getOrderSecurity().checkPermission(OrderSecurity.DELETE, order);
			getOrderRepository().delete(order);
			getJournalService().add(JOURNAL_ORDER_DELETE, JournalUtils.getParams(order));
		}
	}
	
	protected boolean shouldDelete(Order order) {
		return order != null;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonOrders getJsonOrders(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("date must not be null");
		}
		Date from = DateUtils.getStartOfDay(date);
		Date till = DateUtils.getEndOfDay(date);
		List<Order> orders = getOrderRepository().findByFromAndTillOrderByDate(from, till);
		
		if (orders.isEmpty()) {
			JsonOrders json = new JsonOrders();
			json.setDate(date);
			return json;
		}
		
		return toJsonOrders(orders, date);
	}
	
	protected JsonOrders toJsonOrders(List<Order> orders, Date date) {
		JsonOrders json = new JsonOrders();
		json.setDate(date);
		json.setOffer(toJsonOffer(date, orders.get(0).getItem().getOffer()));
		for (Order order : orders) {
			JsonOrder jsonOrder = toJsonOrder(order);
			json.addToOrders(jsonOrder);
		}
		return json;
	}
	
	protected JsonOffer toJsonOffer(Date date, Offer offer) {
		JsonOffer json = new JsonOffer();
		json.setDate(date);
		if (offer != null) {
			json.setId(offer.getId());
			json.setDate(offer.getDate());
			for (OfferItem offerItem : offer.getItems()) {
				json.addToItems(JsonUtils.toJsonOfferItem(offerItem));
			}
		}
		return json;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonOrder getJsonOrder(Long id) {
		Order order = getOrderRepository().findOne(id);
		return toJsonOrder(order);
	}
	
	protected JsonOrder toJsonOrder(Order order) {
		JsonOrdererDetail detail = new JsonOrdererDetail();
		detail.setUserId(order.getOrderer().getId());
		detail.setUserName(order.getOrderer().getFullName());
		detail.setTimes(order.getTimes());
		
		JsonOrder json = new JsonOrder();
		json.setId(order.getId());
		json.setDetail(detail);
		json.setOfferItem(JsonUtils.toJsonOfferItem(order.getItem()));
		return json;
	}
	
	protected JournalService getJournalService() {
		return journalService;
	}
	
	protected OrderRepository getOrderRepository() {
		return orderRepository;
	}
	
	protected OfferItemRepository getOfferItemRepository() {
		return offerItemRepository;
	}

	protected UserRepository getUserRepository() {
		return userRepository;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
	protected OrderSecurity getOrderSecurity() {
		return orderSecurity;
	}
	
	protected Clock getClock() {
		return clock;
	}
	
}
