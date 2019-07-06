package org.brainteam.lunchbox.services;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.domain.Meal;
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
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.DateUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups={"unit"})
public class ReportServiceImplTest {
	
	private static final String DESCRIPTION1 = "mit Spätzle";
	private static final String DESCRIPTION2 = "mit Pommes";
	private static final String DESCRIPTION3 = "mit Semmelknödel";
	private static final String FULLNAME1 = "Katrin Müller-Hohenstein";
	private static final String FULLNAME2 = "Jörg Wontorra";
	private static final String FULLNAME3 = "Bela Rethy";
	private static final String PERSONNELNUMBER1 = "PN1";
	private static final String PERSONNELNUMBER2 = "PN2";
	private static final String HEADLINE1 = "Schweinebraten";
	private static final String HEADLINE2 = "Rinderbraten";
	private static final String HEADLINE3 = "Kalbsbraten";
	private static final Long ID1 = Long.valueOf(1);
	private static final Long ID2 = Long.valueOf(2);
	private static final Long ID3 = Long.valueOf(3);
	private static final Long ID4 = Long.valueOf(4);
	private static final String INGREDIENTS1 = "keine";
	private static final String INGREDIENTS2 = "mehrere";
	private static final String INGREDIENTS3 = "alle";
	private static final Integer MONTH  = Integer.valueOf(7);
	private static final String NAME1 = "Menü 1";
	private static final String NAME2 = "Menü 2";
	private static final Integer PRICEINCENTS = Integer.valueOf(100);
	private static final Integer TIMES1 = Integer.valueOf(1);
	private static final Integer TIMES2 = Integer.valueOf(2);
	private static final Integer YEAR = Integer.valueOf(2013);
	
	private List<Order> ordersForDay1;
	private List<Order> ordersForDay2;
	private List<Order> ordersForUser1;
	private List<Order> allOrders;
	private Set<OfferItem> offerItems;
	private Date date1 = DateUtils.getDay(1, 7, 2013);
	private Date date2 = DateUtils.getDay(31, 7, 2013);
	private Date emptyDate = DateUtils.getDay(1, 1, 1999);
	private Date orderedDate = new Date();
	
	@Mock
	private OfferRepository offerRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private Offer offer1, offer2;
	
	@Mock
	private OfferItem offerItem1, offerItem2, offerItem3;
	
	@Mock
	private Order order1, order2, order3, order4;
	
	@Mock
	private Meal meal1, meal2, meal3;
	
	@Mock
	private User user1, user2, user3;
	
	@Mock
	private Security security;
	
	@Mock
	private SystemConfiguration configuration;

	@InjectMocks
	private ReportServiceImpl reportService = new ReportServiceImpl();
	
	@BeforeClass
	public void setup() {
		MockitoAnnotations.initMocks(this);
		offerItems = new HashSet<>();
		offerItems.add(offerItem2);
		offerItems.add(offerItem3);
		ordersForDay1 = new ArrayList<>();
		ordersForDay1.add(order1);
		ordersForDay1.add(order2);
		ordersForDay2 = new ArrayList<>();
		ordersForDay2.add(order3);
		ordersForDay2.add(order4);
		ordersForUser1 = new ArrayList<>();
		ordersForUser1.add(order1);
		ordersForUser1.add(order3);
		allOrders = new ArrayList<>();
		allOrders.add(order1);
		allOrders.add(order2);
		allOrders.add(order3);
		allOrders.add(order4);
		
		when(offer1.getDate()).thenReturn(date1);
		when(offer1.getId()).thenReturn(ID1);
		when(offer1.getItems()).thenReturn(offerItems);
		
		when(offer2.getDate()).thenReturn(date2);
		when(offer2.getId()).thenReturn(ID2);
		when(offer2.getItems()).thenReturn(Collections.singleton(offerItem3));
		
		when(offerItem1.getId()).thenReturn(ID1);
		when(offerItem1.getMeal()).thenReturn(meal1);
		when(offerItem1.getName()).thenReturn(NAME1);
		when(offerItem1.getOffer()).thenReturn(offer1);
		when(offerItem1.getPriceInCents()).thenReturn(PRICEINCENTS);
		
		when(offerItem2.getId()).thenReturn(ID2);
		when(offerItem2.getMeal()).thenReturn(meal2);
		when(offerItem2.getName()).thenReturn(NAME2);
		when(offerItem2.getOffer()).thenReturn(offer2);
		when(offerItem2.getPriceInCents()).thenReturn(PRICEINCENTS);
		
		when(offerItem3.getId()).thenReturn(ID3);
		when(offerItem3.getMeal()).thenReturn(meal3);
		when(offerItem3.getName()).thenReturn(NAME1);
		when(offerItem3.getOffer()).thenReturn(offer2);
		when(offerItem3.getPriceInCents()).thenReturn(PRICEINCENTS);
		
		when(offerItem2.compareTo(offerItem3)).thenReturn(-1);
		
		when(meal1.getDescription()).thenReturn(DESCRIPTION1);
		when(meal1.getHeadline()).thenReturn(HEADLINE1);
		when(meal1.getId()).thenReturn(ID1);
		when(meal1.getIngredients()).thenReturn(INGREDIENTS1);
		
		when(meal2.getDescription()).thenReturn(DESCRIPTION2);
		when(meal2.getHeadline()).thenReturn(HEADLINE2);
		when(meal2.getId()).thenReturn(ID2);
		when(meal2.getIngredients()).thenReturn(INGREDIENTS2);
		
		when(meal3.getDescription()).thenReturn(DESCRIPTION3);
		when(meal3.getHeadline()).thenReturn(HEADLINE3);
		when(meal3.getId()).thenReturn(ID3);
		when(meal3.getIngredients()).thenReturn(INGREDIENTS3);
		
		when(user1.getId()).thenReturn(ID1);
		when(user1.getFullName()).thenReturn(FULLNAME1);
		when(user1.getPersonnelNumber()).thenReturn(PERSONNELNUMBER1);
		when(user2.getId()).thenReturn(ID2);
		when(user2.getFullName()).thenReturn(FULLNAME2);
		when(user2.getPersonnelNumber()).thenReturn(PERSONNELNUMBER2);
		when(user3.getId()).thenReturn(ID3);
		when(user3.getFullName()).thenReturn(FULLNAME3);
		
		when(user1.compareTo(user2)).thenReturn(-1);
		when(user1.compareTo(user3)).thenReturn(-1);
		when(user2.compareTo(user3)).thenReturn(-1);
		
		when(order1.getId()).thenReturn(ID1);
		when(order1.getItem()).thenReturn(offerItem1);
		when(order1.getOrderedDate()).thenReturn(orderedDate);
		when(order1.getOrderer()).thenReturn(user1);
		when(order1.getTimes()).thenReturn(TIMES1);
		
		when(order2.getId()).thenReturn(ID2);
		when(order2.getItem()).thenReturn(offerItem1);
		when(order2.getOrderedDate()).thenReturn(orderedDate);
		when(order2.getOrderer()).thenReturn(user2);
		when(order2.getTimes()).thenReturn(TIMES2);
		
		when(order3.getId()).thenReturn(ID3);
		when(order3.getItem()).thenReturn(offerItem2);
		when(order3.getOrderedDate()).thenReturn(orderedDate);
		when(order3.getOrderer()).thenReturn(user1);
		when(order3.getTimes()).thenReturn(TIMES1);
		
		when(order4.getId()).thenReturn(ID4);
		when(order4.getItem()).thenReturn(offerItem3);
		when(order4.getOrderedDate()).thenReturn(orderedDate);
		when(order4.getOrderer()).thenReturn(user3);
		when(order4.getTimes()).thenReturn(TIMES2);
		
		when(offerRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(date1), 
				DateUtils.getEndOfDay(date1))).thenReturn(Collections.singletonList(offer1));
		when(offerRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(date2), 
				DateUtils.getEndOfDay(date2))).thenReturn(Collections.singletonList(offer2));
		when(offerRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(emptyDate), 
				DateUtils.getEndOfDay(emptyDate))).thenReturn(new ArrayList<Offer>());
		when(orderRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(date1), 
				DateUtils.getEndOfDay(date1))).thenReturn(ordersForDay1);
		when(orderRepository.findByFromAndTillOrderByDate(DateUtils.getStartOfDay(date2), 
				DateUtils.getEndOfDay(date2))).thenReturn(ordersForDay2);
		when(orderRepository.findByOfferId(ID1)).thenReturn(ordersForDay1);
		when(orderRepository.findByOfferId(ID2)).thenReturn(ordersForDay2);
		when(orderRepository.findByFromAndTillAndLockedAndUserIdOrderByDate(DateUtils.getFirstDayOfMonth(MONTH, YEAR),
				DateUtils.getEndOfMonth(MONTH, YEAR), true, ID1)).thenReturn(ordersForUser1);
		when(orderRepository.findByFromAndTillAndLockedOrderByDate(DateUtils.getFirstDayOfMonth(MONTH, YEAR),
				DateUtils.getEndOfMonth(MONTH, YEAR), true)).thenReturn(allOrders);
	}
	
	@BeforeMethod
	public void resetMocks() {
		reset(security);
		when(security.getCurrentUser()).thenReturn(user1);
		when(security.isCurrentUserAdmin()).thenReturn(Boolean.TRUE);
	}
	
	@Test
	public void testDailyEmpty() {
		JsonOrdersDaily json = reportService.getDailyReportJson(emptyDate);
		assertNotNull(json);
		assertEquals(json.getDate(), emptyDate);
		assertNotNull(json.getItems());
		assertTrue(json.getItems().isEmpty());
	}
	
	@Test
	public void testDailyMultipleOrderersSameItemByDate() {
		JsonOrdersDaily json = reportService.getDailyReportJson(date1);
		assertDate1(json);
	}
	
	@Test
	public void testDailyMultipleOrderersSameItemByOffer() {
		JsonOrdersDaily json = reportService.getDailyReportJson(offer1);
		assertDate1(json);
	}
	
	private void assertDate1(JsonOrdersDaily json) {
		assertNotNull(json);
		assertEquals(json.getDate(), date1);
		
		List<JsonOrdersDailyOfferItem> jsonDailyOfferItems = json.getItems();
		assertNotNull(jsonDailyOfferItems);
		assertEquals(jsonDailyOfferItems.size(), 1);
		
		JsonOrdersDailyOfferItem jsonDailyOfferItem = jsonDailyOfferItems.get(0);
		assertOfferItemInfo(jsonDailyOfferItem.getOfferItem(), offerItem1);
		
		List<JsonOrdererDetail> jsonOrdererDetails = jsonDailyOfferItem.getOrderers();
		assertNotNull(jsonOrdererDetails);
		assertEquals(jsonOrdererDetails.size(), 2);
		assertOrderer(jsonOrdererDetails.get(0), user1, order1.getTimes());
		assertOrderer(jsonOrdererDetails.get(1), user2, order2.getTimes());
	}
	
	@Test
	public void testDailyMultipleOrderersDifferentItemByDate() {
		JsonOrdersDaily json = reportService.getDailyReportJson(date2);
		assertDate2(json);
	}
	
	@Test
	public void testDailyMultipleOrderersDifferentItemsByOffer() {
		JsonOrdersDaily json = reportService.getDailyReportJson(offer2);
		assertDate2(json);
	}
	
	private void assertDate2(JsonOrdersDaily json) {
		assertNotNull(json);
		assertEquals(json.getDate(), date2);
		
		List<JsonOrdersDailyOfferItem> jsonDailyOfferItems = json.getItems();
		assertNotNull(jsonDailyOfferItems);
		assertEquals(jsonDailyOfferItems.size(), 2);
		
		// Achtung Sortierung!
		JsonOrdersDailyOfferItem jsonDailyOfferItem = jsonDailyOfferItems.get(0);
		assertOfferItemInfo(jsonDailyOfferItem.getOfferItem(), offerItem2);
		List<JsonOrdererDetail> jsonOrdererDetails = jsonDailyOfferItem.getOrderers();
		assertNotNull(jsonOrdererDetails);
		assertEquals(jsonOrdererDetails.size(), 1);
		assertOrderer(jsonOrdererDetails.get(0), user1, order3.getTimes());
		
		jsonDailyOfferItem = jsonDailyOfferItems.get(1);
		assertOfferItemInfo(jsonDailyOfferItem.getOfferItem(), offerItem3);
		jsonOrdererDetails = jsonDailyOfferItem.getOrderers();
		assertNotNull(jsonOrdererDetails);
		assertEquals(jsonOrdererDetails.size(), 1);
		assertOrderer(jsonOrdererDetails.get(0), user3, order4.getTimes());
	}
	
	@Test(dataProvider="trueFalse")
	public void testMonthly(boolean admin) {
		when(security.isCurrentUserAdmin()).thenReturn(Boolean.valueOf(admin));
		
		JsonOrdersMonthly json = reportService.getMonthlyReportJson(MONTH, YEAR);
		assertMonthlyUser1(json, admin);
	}
	
	@Test(dataProvider="trueFalse")
	public void testBilling(boolean admin) {
		when(security.isCurrentUserAdmin()).thenReturn(Boolean.valueOf(admin));
		
		JsonOrdersBilling json = reportService.getBillingReportJson(MONTH, YEAR);
		assertNotNull(json);
		assertEquals(json.getMonth(), MONTH);
		assertEquals(json.getYear(), YEAR);
		assertEquals(json.getSum(), Integer.valueOf(6 * PRICEINCENTS));
		
		List<JsonOrdersMonthly> jsonOrdersMonthlys = json.getItems();
		assertNotNull(jsonOrdersMonthlys);
		assertEquals(jsonOrdersMonthlys.size(), 3);
		
		JsonOrdersMonthly jsonOrdersMonthly = jsonOrdersMonthlys.get(0);
		assertMonthlyUser1(jsonOrdersMonthly, admin);
		
		jsonOrdersMonthly = jsonOrdersMonthlys.get(1);
		assertMonthlyUser2(jsonOrdersMonthly, admin);
		
		jsonOrdersMonthly = jsonOrdersMonthlys.get(2);
		assertMonthlyUser3(jsonOrdersMonthly);
	}
	
	protected static void assertOfferItemInfo(JsonOfferItemInfo json, OfferItem offerItem) {
		assertNotNull(json);
		assertEquals(json.getDescription(), offerItem.getMeal().getDescription());
		assertEquals(json.getHeadline(), offerItem.getMeal().getHeadline());
		assertEquals(json.getId(), offerItem.getId());
		assertEquals(json.getIngredients(), offerItem.getMeal().getIngredients());
		assertEquals(json.getMealId(), offerItem.getMeal().getId());
		assertEquals(json.getName(), offerItem.getName());
		assertEquals(json.getPriceInCents(), offerItem.getPriceInCents());
	}
	
	protected static void assertOrderer(JsonOrdererDetail json, User user, Integer times) {
		assertNotNull(json);
		assertEquals(json.getTimes(), times);
		assertEquals(json.getUserId(), user.getId());
		assertEquals(json.getUserName(), user.getFullName());
	}
	
	protected void assertMonthlyUser1(JsonOrdersMonthly json) {
		assertMonthlyUser1(json, true);
	}
	
	protected void assertMonthlyUser1(JsonOrdersMonthly json, boolean admin) {
		assertNotNull(json);
		assertEquals(json.getMonth(), MONTH);
		assertEquals(json.getYear(), YEAR);
		assertEquals(json.getPersonnelNumber(), admin ? PERSONNELNUMBER1 : null);
		assertEquals(json.getUserFullname(), FULLNAME1);
		assertEquals(json.getSum(), Long.valueOf(2 * PRICEINCENTS));
		assertNotNull(json.getDays());
		assertEquals(json.getDays().size(), 31);
		
		JsonOrdersDayOfMonth jsonOrdersDayOfMonth = json.getDays().get(0);
		assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(1, MONTH, YEAR));
		assertNotNull(jsonOrdersDayOfMonth.getItems());
		assertEquals(jsonOrdersDayOfMonth.getItems().size(), 1);
		assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(PRICEINCENTS));
		
		JsonOrdersMonthlyOrderItem jsonOrdersMonthlyOrderItem = jsonOrdersDayOfMonth.getItems().get(0);
		assertNotNull(jsonOrdersMonthlyOrderItem);
		assertOfferItemInfo(jsonOrdersMonthlyOrderItem.getOfferItem(), offerItem1);
		assertEquals(jsonOrdersMonthlyOrderItem.getTimes(), TIMES1);
		assertEquals(jsonOrdersMonthlyOrderItem.getPriceInCents(), Long.valueOf(PRICEINCENTS));
		
		for (int i=1; i < 30; i++) {
			jsonOrdersDayOfMonth = json.getDays().get(i);
			assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(i+1, MONTH, YEAR));
			assertNotNull(jsonOrdersDayOfMonth.getItems());
			assertTrue(jsonOrdersDayOfMonth.getItems().isEmpty());
			assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(0));
		}
		
		jsonOrdersDayOfMonth = json.getDays().get(30);
		assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(31, MONTH, YEAR));
		assertNotNull(jsonOrdersDayOfMonth.getItems());
		assertEquals(jsonOrdersDayOfMonth.getItems().size(), 1);
		assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(PRICEINCENTS));
		
		jsonOrdersMonthlyOrderItem = jsonOrdersDayOfMonth.getItems().get(0);
		assertNotNull(jsonOrdersMonthlyOrderItem);
		assertOfferItemInfo(jsonOrdersMonthlyOrderItem.getOfferItem(), offerItem2);
		assertEquals(jsonOrdersMonthlyOrderItem.getTimes(), TIMES1);
		assertEquals(jsonOrdersMonthlyOrderItem.getPriceInCents(), Long.valueOf(PRICEINCENTS));
	}
	
	protected void assertMonthlyUser2(JsonOrdersMonthly json, boolean admin) {
		assertNotNull(json);
		assertEquals(json.getMonth(), MONTH);
		assertEquals(json.getYear(), YEAR);
		assertEquals(json.getPersonnelNumber(), admin ? PERSONNELNUMBER2 : null);
		assertEquals(json.getUserFullname(), FULLNAME2);
		assertEquals(json.getSum(), Long.valueOf(2 * PRICEINCENTS));
		assertNotNull(json.getDays());
		assertEquals(json.getDays().size(), 31);
		
		JsonOrdersDayOfMonth jsonOrdersDayOfMonth = json.getDays().get(0);
		assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(1, MONTH, YEAR));
		assertNotNull(jsonOrdersDayOfMonth.getItems());
		assertEquals(jsonOrdersDayOfMonth.getItems().size(), 1);
		assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(2 * PRICEINCENTS));
		
		JsonOrdersMonthlyOrderItem jsonOrdersMonthlyOrderItem = jsonOrdersDayOfMonth.getItems().get(0);
		assertNotNull(jsonOrdersMonthlyOrderItem);
		assertOfferItemInfo(jsonOrdersMonthlyOrderItem.getOfferItem(), offerItem1);
		assertEquals(jsonOrdersMonthlyOrderItem.getTimes(), TIMES2);
		assertEquals(jsonOrdersMonthlyOrderItem.getPriceInCents(), Long.valueOf(2 * PRICEINCENTS));
		
		for (int i=1; i < 31; i++) {
			jsonOrdersDayOfMonth = json.getDays().get(i);
			assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(i+1, MONTH, YEAR));
			assertNotNull(jsonOrdersDayOfMonth.getItems());
			assertTrue(jsonOrdersDayOfMonth.getItems().isEmpty());
			assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(0));
		}
	}
	
	protected void assertMonthlyUser3(JsonOrdersMonthly json) {
		assertNotNull(json);
		assertEquals(json.getMonth(), MONTH);
		assertEquals(json.getYear(), YEAR);
		assertEquals(json.getUserFullname(), FULLNAME3);
		assertEquals(json.getSum(), Long.valueOf(2 * PRICEINCENTS));
		assertNotNull(json.getDays());
		assertEquals(json.getDays().size(), 31);
		
		JsonOrdersDayOfMonth jsonOrdersDayOfMonth;
		for (int i=0; i < 30; i++) {
			jsonOrdersDayOfMonth = json.getDays().get(i);
			assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(i+1, MONTH, YEAR));
			assertNotNull(jsonOrdersDayOfMonth.getItems());
			assertTrue(jsonOrdersDayOfMonth.getItems().isEmpty());
			assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(0));
		}
		
		jsonOrdersDayOfMonth = json.getDays().get(30);
		assertEquals(jsonOrdersDayOfMonth.getDate(), DateUtils.getDay(31, MONTH, YEAR));
		assertNotNull(jsonOrdersDayOfMonth.getItems());
		assertEquals(jsonOrdersDayOfMonth.getItems().size(), 1);
		assertEquals(jsonOrdersDayOfMonth.getSum(), Long.valueOf(2 * PRICEINCENTS));
		
		JsonOrdersMonthlyOrderItem jsonOrdersMonthlyOrderItem = jsonOrdersDayOfMonth.getItems().get(0);
		assertNotNull(jsonOrdersMonthlyOrderItem);
		assertOfferItemInfo(jsonOrdersMonthlyOrderItem.getOfferItem(), offerItem3);
		assertEquals(jsonOrdersMonthlyOrderItem.getTimes(), TIMES2);
		assertEquals(jsonOrdersMonthlyOrderItem.getPriceInCents(), Long.valueOf(2 * PRICEINCENTS));
	}
	
	@DataProvider(name="trueFalse")
	public Object[][] createData() {
		return new Object[][] { {true}, {false} } ;
	}
	
}
