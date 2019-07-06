package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.dao.StatisticsDAO;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.i18n.CurrencyFormatter;
import org.brainteam.lunchbox.i18n.Translator;
import org.brainteam.lunchbox.json.JsonPieChartData;
import org.brainteam.lunchbox.json.JsonStatisticsNamedValueItem;
import org.brainteam.lunchbox.json.JsonSystemStatistics;
import org.brainteam.lunchbox.json.JsonTopUser;
import org.brainteam.lunchbox.json.JsonUserStatistics;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.Pair;
import org.brainteam.lunchbox.util.Triplet;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups="unit")
public class StatisticsServiceImplTest {
	
	private static final String FORMATTED = "formatted";
	private static final String NAME = "Januar";
	private static final Long MEAL_COUNT = Long.valueOf(92);
	private static final Long OFFER_COUNT = Long.valueOf(34);
	private static final Long ORDER_COUNT = Long.valueOf(100);
	private static final Long ORDERED_ITEMS_COUNT = Long.valueOf(210);
	private static final String TRANSLATED = "translated";
	private static final Long VALUE = Long.valueOf(3);
	
	private Pair<String,Long> NAMED_VALUE = new Pair<>(NAME, VALUE);
	private List<Pair<String,Long>> NAMED_VALUES = Collections.singletonList(NAMED_VALUE);
	private List<Triplet<Integer,Integer,Long>> SALES_BY_MONTH = Collections.singletonList(new Triplet<>(1, 1, VALUE));
	
	@Mock
	private MealRepository mealRepository;
	
	@Mock
	private OfferRepository offerRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private StatisticsDAO statisticsDAO;
	
	@Mock
	private Security security;
	
	@Mock
	private Translator translator;
	
	@Mock
	private User user;
	
	@Mock
	private CurrencyFormatter currencyFormatter;

	@InjectMocks
	private StatisticServiceImpl statisticService = new StatisticServiceImpl();
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(currencyFormatter.format(any(Long.class))).thenReturn(FORMATTED);
		when(mealRepository.count()).thenReturn(MEAL_COUNT);
		when(offerRepository.count()).thenReturn(OFFER_COUNT);
		when(orderRepository.count()).thenReturn(ORDER_COUNT);
		when(orderRepository.countTimes()).thenReturn(ORDERED_ITEMS_COUNT);
		when(orderRepository.count(any(Long.class))).thenReturn(ORDER_COUNT);
		when(orderRepository.countTimes(any(Long.class))).thenReturn(ORDERED_ITEMS_COUNT);
		when(security.getCurrentUser()).thenReturn(user);
		when(statisticsDAO.getGreediestUsers(any(Integer.class))).thenReturn(NAMED_VALUES);
		when(statisticsDAO.getOrderDistributionByName(any(Long.class))).thenReturn(NAMED_VALUES);
		when(statisticsDAO.getOrderDistributionByName()).thenReturn(NAMED_VALUES);
		when(statisticsDAO.getPopularMeals(any(Integer.class))).thenReturn(NAMED_VALUES);
		when(statisticsDAO.getPopularMeals(any(Long.class), any(Integer.class))).thenReturn(NAMED_VALUES);
		when(statisticsDAO.getSalesByMonth(any(Integer.class))).thenReturn(SALES_BY_MONTH);
		when(statisticsDAO.getSalesByMonth(any(Long.class), any(Integer.class))).thenReturn(SALES_BY_MONTH);
		when(translator.t(any(String.class))).thenReturn(TRANSLATED);
	}
	
	@Test
	public void testUserStats() {
		JsonUserStatistics json = statisticService.getCurrentUserStatistics();
		assertNotNull(json);
		
		Object[] menuDistribution = json.getMenuDistribution();
		assertMenuDistribution(menuDistribution);
		
		List<JsonStatisticsNamedValueItem> jsonNamedValues = json.getNamedValues();
		assertNotNull(jsonNamedValues);
		assertEquals(jsonNamedValues.size(), 3);
		
		JsonStatisticsNamedValueItem jsonNamedValue = jsonNamedValues.get(0);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(ORDER_COUNT));
		
		jsonNamedValue = jsonNamedValues.get(1);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(ORDERED_ITEMS_COUNT));
		
		jsonNamedValue = jsonNamedValues.get(2);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), FORMATTED);
		
		List<JsonStatisticsNamedValueItem> jsonPopularMenus = json.getPopularMeals();
		assertPopularMeals(jsonPopularMenus);
		
		Object[] salesByMonth = json.getSalesByMonth();
		assertSalesByMonth(salesByMonth);
	}
	
	@Test
	public void testSystemStats() {
		JsonSystemStatistics json = statisticService.getSystemStatistics();
		assertNotNull(json);
		
		List<JsonStatisticsNamedValueItem> jsonGreediestUserItems = json.getGreediestUsers();
		assertNotNull(jsonGreediestUserItems);
		assertEquals(jsonGreediestUserItems.size(), 1);
		
		JsonStatisticsNamedValueItem jsonGreediestUser = jsonGreediestUserItems.get(0);
		assertNamedValue(jsonGreediestUser);
		
		Object[] menuDistribution = json.getMenuDistribution();
		assertMenuDistribution(menuDistribution);
		
		List<JsonStatisticsNamedValueItem> jsonNamedValues = json.getNamedValues();
		assertNotNull(jsonNamedValues);
		assertEquals(jsonNamedValues.size(), 5);
		
		JsonStatisticsNamedValueItem jsonNamedValue = jsonNamedValues.get(0);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(ORDER_COUNT));
		
		jsonNamedValue = jsonNamedValues.get(1);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(ORDERED_ITEMS_COUNT));
		
		jsonNamedValue = jsonNamedValues.get(2);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), FORMATTED);
		
		jsonNamedValue = jsonNamedValues.get(3);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(MEAL_COUNT));
		
		jsonNamedValue = jsonNamedValues.get(4);
		assertNotNull(jsonNamedValue);
		assertEquals(jsonNamedValue.getName(), TRANSLATED);
		assertEquals(jsonNamedValue.getValue(), String.valueOf(OFFER_COUNT));
		
		List<JsonStatisticsNamedValueItem> jsonPopularMeals = json.getPopularMeals();
		assertPopularMeals(jsonPopularMeals);
		
		Object[] salesByMonth = json.getSalesByMonth();
		assertSalesByMonth(salesByMonth);
		
		List<JsonTopUser> jsonTopUsers = json.getTopUsers();
		assertNotNull(jsonTopUsers);
		assertEquals(jsonTopUsers.size(), 0);
	}
	
	@Test
	public void testTopUsers() {
		when(statisticsDAO.getChineseTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getFishTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getGourmetTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getGreediestUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getItalianTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getMamaTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getSaladTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getSchnitzelTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getSwabianTopUser()).thenReturn(NAMED_VALUE);
		when(statisticsDAO.getVeggieTopUser()).thenReturn(NAMED_VALUE);
		
		JsonSystemStatistics json = statisticService.getSystemStatistics();
		assertNotNull(json);
		
		List<JsonTopUser> jsonTopUsers = json.getTopUsers();
		assertNotNull(jsonTopUsers);
		assertEquals(jsonTopUsers.size(), 10);
		
		for (JsonTopUser jsonTopUser : jsonTopUsers) {
			assertNotNull(jsonTopUser);
			assertEquals(jsonTopUser.getDescription(), TRANSLATED);
			assertEquals(jsonTopUser.getName(), TRANSLATED);
			assertNotNull(jsonTopUser.getTag());
			assertEquals(jsonTopUser.getTimes(), VALUE);
			assertEquals(jsonTopUser.getUserName(), NAME);
		}
	}
	
	protected static void assertMenuDistribution(Object[] menuDistribution) {
		assertNotNull(menuDistribution);
		assertEquals(menuDistribution.length, 1);
		
		Object menuDistributionItem1 = menuDistribution[0];
		assertTrue(menuDistributionItem1 instanceof JsonPieChartData);
		
		JsonPieChartData jsonPieChartData = (JsonPieChartData)menuDistributionItem1;
		assertEquals(jsonPieChartData.getLabel(), NAME);
		assertEquals(jsonPieChartData.getData(), VALUE);
	}
	
	protected static void assertPopularMeals(List<JsonStatisticsNamedValueItem> json) {
		assertNotNull(json);
		assertEquals(json.size(), 1);
		
		JsonStatisticsNamedValueItem jsonPopularMenu = json.get(0);
		assertNamedValue(jsonPopularMenu);
	}
	
	protected static void assertSalesByMonth(Object[] salesByMonth) {
		assertNotNull(salesByMonth);
		assertEquals(salesByMonth.length, 1);
		
		Object salesByMonthDistributionItem1 = salesByMonth[0];
		assertTrue(salesByMonthDistributionItem1 instanceof Object[][]);
		
		Object[][] data = (Object[][])salesByMonthDistributionItem1;
		assertEquals(data[0][0], "Januar");
		assertEquals(data[0][1], Long.valueOf(3));
	}
	
	protected static void assertNamedValue(JsonStatisticsNamedValueItem json) {
		assertNotNull(json);
		assertEquals(json.getName(), NAME);
		assertEquals(json.getValue(), String.valueOf(VALUE));
	}
	
}
