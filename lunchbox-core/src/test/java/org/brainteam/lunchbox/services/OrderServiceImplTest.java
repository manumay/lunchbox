package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.brainteam.lunchbox.core.Clock;
import org.brainteam.lunchbox.core.LunchboxRuntimeException;
import org.brainteam.lunchbox.dao.OfferItemRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonOfferItemInfo;
import org.brainteam.lunchbox.json.JsonOfferOrder;
import org.brainteam.lunchbox.json.JsonOrder;
import org.brainteam.lunchbox.json.JsonOrdererDetail;
import org.brainteam.lunchbox.json.JsonOrders;
import org.brainteam.lunchbox.security.OrderSecurity;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.DateUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups="unit")
public class OrderServiceImplTest {
	
	private static final Long ID1 = Long.valueOf(1);
	private static final Long ID2 = Long.valueOf(2);
	private static final Integer TIMES = Integer.valueOf(1);
	private static final String MEAL_HEADLINE = "Sauerbraten";
	private static final String MEAL_DESCRIPTION = "mit Semmelknödel";
	private static final String MEAL_INGREDIENTS = "Bla";
	private static final String OFFERITEM_NAME = "Menü";
	private static final Integer OFFERITEM_PRICEINCENTS = Integer.valueOf(100);
	private static final String USER_FULLNAME1 = "Jean-Claude van Damme";
	private static final String USER_FULLNAME2 = "Jason Statham";
	private static final Long NOT_EXISTING_ID = Long.valueOf(9999);
	
	private List<Order> orders = new ArrayList<Order>();
	
	private Date date = new Date();
	private Date emptyDate = DateUtils.getDay(1, 1, 2000);
	
	@Mock
	private OrderSecurity orderSecurity;

	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private OfferItemRepository offerItemRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private JournalService journalService;
	
	@Mock
	private Security security;
	
	@Mock
	private Clock clock;
	
	@Mock
	private Order orderWithId, anotherOrder;
	
	@Mock
	private OfferItem offerItem;
	
	@Mock
	private Meal meal;
	
	@Mock
	private User currentUser, user, anotherUser;
	
	@Mock
	private Offer offer;
	
	@Mock
	private JsonOfferOrder jsonOfferOrderNew, jsonOfferOrderUpdate, jsonOfferOrderNewMissingOfferItem, jsonOfferOrderUpdateMissingOfferItem, 
		jsonOfferOrderNewNonExistingOfferItem, jsonOfferOrderUpdateNonExistingOfferItem;
	
	@InjectMocks
	private OrderServiceImpl orderService = new OrderServiceImpl();
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		reset(journalService, orderRepository);
		
		orders = new ArrayList<>();
		orders.add(orderWithId);
		orders.add(anotherOrder);
		
		when(orderRepository.save(any(Order.class))).thenReturn(orderWithId);
		when(orderRepository.findOne(ID1)).thenReturn(orderWithId);
		when(orderRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(date), 
				DateUtils.getEndOfDay(date))).thenReturn(orders);
		when(orderRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(emptyDate), 
				DateUtils.getEndOfDay(emptyDate))).thenReturn(new ArrayList<Order>());
		when(offerItemRepository.findOne(ID1)).thenReturn(offerItem);
		when(offerItemRepository.findOne(NOT_EXISTING_ID)).thenReturn(null);
		when(userRepository.findOne(ID1)).thenReturn(user);
		when(userRepository.findOne(ID2)).thenReturn(anotherUser);
		when(security.getCurrentUser()).thenReturn(currentUser);
		when(clock.now()).thenReturn(date);
		
		when(meal.getId()).thenReturn(ID1);
		when(meal.getHeadline()).thenReturn(MEAL_HEADLINE);
		when(meal.getDescription()).thenReturn(MEAL_DESCRIPTION);
		when(meal.getIngredients()).thenReturn(MEAL_INGREDIENTS);
		
		when(user.getId()).thenReturn(ID1);
		when(user.getFullName()).thenReturn(USER_FULLNAME1);
		
		when(anotherUser.getId()).thenReturn(ID2);
		when(anotherUser.getFullName()).thenReturn(USER_FULLNAME2);
		
		when(offerItem.getId()).thenReturn(ID1);
		when(offerItem.getName()).thenReturn(OFFERITEM_NAME);
		when(offerItem.getPriceInCents()).thenReturn(OFFERITEM_PRICEINCENTS);
		when(offerItem.getMeal()).thenReturn(meal);
		when(offerItem.getOffer()).thenReturn(offer);
		
		when(offer.getDate()).thenReturn(date);
		when(offer.getItems()).thenReturn(Collections.singleton(offerItem));
		
		when(orderWithId.getId()).thenReturn(ID1);
		when(orderWithId.getItem()).thenReturn(offerItem);
		when(orderWithId.getOrderedDate()).thenReturn(date);
		when(orderWithId.getOrderer()).thenReturn(user);
		when(orderWithId.getTimes()).thenReturn(TIMES);
		
		when(anotherOrder.getId()).thenReturn(ID2);
		when(anotherOrder.getItem()).thenReturn(offerItem);
		when(anotherOrder.getOrderedDate()).thenReturn(date);
		when(anotherOrder.getOrderer()).thenReturn(anotherUser);
		when(anotherOrder.getTimes()).thenReturn(TIMES);
		
		when(jsonOfferOrderNew.getId()).thenReturn(null);
		when(jsonOfferOrderNew.getOfferItemId()).thenReturn(ID1);
		when(jsonOfferOrderNew.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderNew.getUserId()).thenReturn(ID1);

		when(jsonOfferOrderNewMissingOfferItem.getId()).thenReturn(null);
		when(jsonOfferOrderNewMissingOfferItem.getOfferItemId()).thenReturn(null);
		when(jsonOfferOrderNewMissingOfferItem.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderNewMissingOfferItem.getUserId()).thenReturn(ID1);
		
		when(jsonOfferOrderNewNonExistingOfferItem.getId()).thenReturn(null);
		when(jsonOfferOrderNewNonExistingOfferItem.getOfferItemId()).thenReturn(NOT_EXISTING_ID);
		when(jsonOfferOrderNewNonExistingOfferItem.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderNewNonExistingOfferItem.getUserId()).thenReturn(ID1);
		
		when(jsonOfferOrderUpdate.getId()).thenReturn(ID1);
		when(jsonOfferOrderUpdate.getOfferItemId()).thenReturn(ID1);
		when(jsonOfferOrderUpdate.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderUpdate.getUserId()).thenReturn(ID1);
		
		when(jsonOfferOrderUpdateMissingOfferItem.getId()).thenReturn(ID1);
		when(jsonOfferOrderUpdateMissingOfferItem.getOfferItemId()).thenReturn(null);
		when(jsonOfferOrderUpdateMissingOfferItem.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderUpdateMissingOfferItem.getUserId()).thenReturn(ID1);
		
		when(jsonOfferOrderUpdateNonExistingOfferItem.getId()).thenReturn(ID1);
		when(jsonOfferOrderUpdateNonExistingOfferItem.getOfferItemId()).thenReturn(NOT_EXISTING_ID);
		when(jsonOfferOrderUpdateNonExistingOfferItem.getTimes()).thenReturn(TIMES);
		when(jsonOfferOrderUpdateNonExistingOfferItem.getUserId()).thenReturn(ID1);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testCreateNewNull() {
		orderService.createNew(null);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testUpdateExistingNull() {
		orderService.updateExisting(null);
	}
	
	@Test
	public void testDeleteExistingNull() {
		orderService.deleteExisting(null);
		verify(orderRepository, never()).delete(any(Long.class));
		verify(journalService, never()).add(eq(OrderServiceImpl.JOURNAL_ORDER_DELETE), any(String[].class));
	}
	
	@Test
	public void testCreateNew() {
		orderService.createNew(jsonOfferOrderNew);
		
		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderRepository).save(captor.capture());
		
		Order order = captor.getValue();
		assertNotNull(order);
		assertEquals(order.getItem(), offerItem);
		assertEquals(order.getOrderedDate(), date);
		assertEquals(order.getOrderer(), user);
		assertEquals(order.getTimes(), TIMES);
		
		verify(journalService).add(eq(OrderServiceImpl.JOURNAL_ORDER_PUT), any(String[].class));
	}
	
	@Test(expectedExceptions=LunchboxRuntimeException.class)
	public void testCreateNewMissingOfferItemId() {
		orderService.createNew(jsonOfferOrderNewMissingOfferItem);
	}
	
	@Test
	public void testCreateNewNonExistingOfferitem() {
		orderService.createNew(jsonOfferOrderNewNonExistingOfferItem);
		verify(orderRepository, never()).save(any(Order.class));
		verify(journalService, never()).add(any(String.class), any(String[].class));
	}
	
	@Test(expectedExceptions=LunchboxRuntimeException.class)
	public void testCreateNewOfferItemSecurityFailure() {
		doThrow(new LunchboxRuntimeException()).when(orderSecurity).checkPermission(any(String.class), any(Order.class));
		orderService.createNew(jsonOfferOrderNew);
	}
	
	@Test
	public void testUpdateExisting() {
		orderService.updateExisting(jsonOfferOrderUpdate);
		
		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderRepository).save(captor.capture());
		
		Order order = captor.getValue();
		assertNotNull(order);
		assertEquals(order.getId(), ID1);
		assertEquals(order.getItem(), offerItem);
		assertEquals(order.getOrderedDate(), date);
		assertEquals(order.getOrderer(), user);
		assertEquals(order.getTimes(), TIMES);
		
		verify(journalService).add(eq(OrderServiceImpl.JOURNAL_ORDER_POST), any(String[].class));
	}
	
	@Test(expectedExceptions=LunchboxRuntimeException.class)
	public void testUpdateExistingMissingOfferItem() {
		orderService.updateExisting(jsonOfferOrderUpdateMissingOfferItem);
	}
	
	@Test(expectedExceptions=LunchboxRuntimeException.class)
	public void testUpdateExistingMissingId() {
		orderService.updateExisting(jsonOfferOrderNew);
	}
	
	@Test
	public void testUpdateExistingNonExistingOfferitem() {
		orderService.updateExisting(jsonOfferOrderUpdateNonExistingOfferItem);
		verify(orderRepository, never()).save(any(Order.class));
		verify(journalService, never()).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testDeleteExisting() {
		orderService.deleteExisting(ID1);
		verify(orderRepository).delete(orderWithId);
		verify(journalService).add(eq(OrderServiceImpl.JOURNAL_ORDER_DELETE), any(String[].class));
	}
	
	@Test
	public void testDeleteNotExisting() {
		orderService.deleteExisting(NOT_EXISTING_ID);
		verify(orderRepository, never()).delete(any(Order.class));
		verify(journalService, never()).add(eq(OrderServiceImpl.JOURNAL_ORDER_DELETE), any(String[].class));
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testGetJsonOrdersNull() {
		orderService.getJsonOrders(null);
	}
	
	@Test
	public void testGetJsonOrders() {
		JsonOrders json = orderService.getJsonOrders(date);
		assertNotNull(json);
		assertEquals(json.getDate(), date);
		assertNotNull(json.getOffer());
		assertEquals(json.getOffer().getDate(), offer.getDate());
		
		List<JsonOfferItemInfo> jsonOfferItems = json.getOffer().getItems();
		assertNotNull(jsonOfferItems);
		assertEquals(jsonOfferItems.size(), 1);
		
		JsonOfferItemInfo jsonOfferItemInfo = jsonOfferItems.get(0);
		assertJsonOfferItemInfo(jsonOfferItemInfo);
		
		List<JsonOrder> jsonOrders = json.getOrders();
		assertNotNull(jsonOrders);
		assertEquals(jsonOrders.size(), 2);
		
		JsonOrder jsonOrder = jsonOrders.get(0);
		assertEquals(jsonOrder.getId(), ID1);
		
		JsonOrdererDetail jsonOrdererDetail = jsonOrder.getDetail();
		assertEquals(jsonOrdererDetail.getUserId(), ID1);
		assertEquals(jsonOrdererDetail.getTimes(), TIMES);
		assertEquals(jsonOrdererDetail.getUserName(), USER_FULLNAME1);
		assertJsonOfferItemInfo(jsonOrder.getOfferItem());
		
		jsonOrder = jsonOrders.get(1);
		
		jsonOrdererDetail = jsonOrder.getDetail();
		assertEquals(jsonOrdererDetail.getUserId(), ID2);
		assertEquals(jsonOrdererDetail.getTimes(), TIMES);
		assertEquals(jsonOrdererDetail.getUserName(), USER_FULLNAME2);
		assertJsonOfferItemInfo(jsonOrder.getOfferItem());
	}
	
	@Test
	public void testGetJsonOrdersEmpty() {
		JsonOrders json = orderService.getJsonOrders(emptyDate);
		assertNotNull(json);
		assertEquals(json.getDate(), emptyDate);
		assertNull(json.getOffer());
		assertNotNull(json.getOrders());
		assertTrue(json.getOrders().isEmpty());
	}

	protected static void assertJsonOfferItemInfo(JsonOfferItemInfo json) {
		assertEquals(json.getId(), ID1);
		assertEquals(json.getMealId(), ID1);
		assertEquals(json.getName(), OFFERITEM_NAME);
		assertEquals(json.getHeadline(), MEAL_HEADLINE);
		assertEquals(json.getDescription(), MEAL_DESCRIPTION);
		assertEquals(json.getIngredients(), MEAL_INGREDIENTS);
		assertEquals(json.getPriceInCents(), OFFERITEM_PRICEINCENTS);
	}
}
