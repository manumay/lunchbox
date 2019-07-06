package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.OrderRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;
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
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.test.TestUtils;
import org.brainteam.lunchbox.util.DateUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups={"unit"})
public class OfferServiceImplTest {
	
	private static final String DESCRIPTION = "description";
	private static final String HEADLINE = "headline";
	private static final String INGREDIENTS = "ingredients";
	private static final Integer PRICEINCENTS = Integer.valueOf(100);
	private static final Long ID = Long.valueOf(1);
	private static final String IDTEXT = String.valueOf(ID);
	private static final String NAME = "name";
	private static final Integer TIMES = Integer.valueOf(2);
	private static final String TYPE = "type";
	private static final String LUNCHTIME = "12";
	private static final Date DATE_EXISTING = DateUtils.getDay(1, 1, 1999);
	private static final Date DATE_EXISTING_START = DateUtils.getStartOfDay(DATE_EXISTING);
	private static final Date DATE_EXISTING_END = DateUtils.getEndOfDay(DATE_EXISTING);
	private static final Date DATE_NOT_EXISTING = DateUtils.getDay(1, 1, 2000);
	private static final Date DATE_NOT_EXISTING_START = DateUtils.getStartOfDay(DATE_NOT_EXISTING);
	private static final Date DATE_NOT_EXISTING_END = DateUtils.getEndOfDay(DATE_NOT_EXISTING);
	private static final Date DATE_NOT_EXISTING_EXPECTED = DateUtils.getWithHourOfDay(DATE_NOT_EXISTING, 12);
	private static final Date DATE_DAILY_OFFER = DateUtils.getDay(1, 1, 2001);
	private static final Date DATE_PERIODIC_OFFER_START = DateUtils.getDay(2, 1, 2001);
	private static final Date DATE_PERIODIC_OFFER_END = DateUtils.getDay(3, 1, 2001);
	private static final URL TEST_URL = TestUtils.getURL("http://www.test.de");
	
	@Spy
	private List<Offer> existingOffers = new ArrayList<>();
	
	@Spy
	private List<Order> existingOrders = new ArrayList<>();
	
	@Spy
	private Set<OfferItem> existingOfferItems = new HashSet<>();
	
	@Spy
	private List<OfferDailyDefinition> dailyOffers = new ArrayList<>();
	
	@Spy
	private List<OfferPeriodicDefinition> periodicOffers = new ArrayList<>();
	
	@Spy
	private List<MealDefinition> mealDefs = new ArrayList<>();
	
	@Mock
	private Offer offerWithId;
	
	@Mock
	private Order order;
	
	@Mock
	private OfferItem offerItem;
	
	@Mock
	private Meal meal;
	
	@Mock
	private User user;
	
	@Mock
	private SystemConfiguration systemConfiguration;
	
	@Mock
	private JournalService journalService;
	
	@Mock
	private OfferItemService offerItemService;
	
	@Mock
	private OfferRepository offerRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private Security security;
	
	@Mock
	private Downloader downloader;
	
	@Mock
	private MenuParser parser;
	
	@Mock
	private OffersDefinition offersDefinition;
	
	@Mock
	private OfferDailyDefinition dailyOffer;
	
	@Mock
	private OfferPeriodicDefinition periodicOffer;
	
	@Mock
	private MealDefinition mealDefinition;
	
	@InjectMocks
	private OfferServiceImpl offerService = new OfferServiceImpl();
	
	@BeforeClass
    public void init() {
		MockitoAnnotations.initMocks(this);
		existingOffers.add(offerWithId);
		existingOrders.add(order);
		existingOfferItems.add(offerItem);
		dailyOffers.add(dailyOffer);
		periodicOffers.add(periodicOffer);
		mealDefs.add(mealDefinition);
		when(systemConfiguration.getLunchtime()).thenReturn(LUNCHTIME);
		when(security.getCurrentUser()).thenReturn(user);
    }
	
	@BeforeMethod
	public void resetMocks() throws IOException {
		reset(offerWithId, offerRepository, orderRepository, journalService);
		when(meal.getId()).thenReturn(ID);
		when(meal.getHeadline()).thenReturn(HEADLINE);
		when(meal.getIngredients()).thenReturn(INGREDIENTS);
		when(meal.getDescription()).thenReturn(DESCRIPTION);
		when(offerItem.getId()).thenReturn(ID);
		when(offerItem.getMeal()).thenReturn(meal);
		when(offerItem.getName()).thenReturn(NAME);
		when(offerItem.getPriceInCents()).thenReturn(PRICEINCENTS);
		when(offerItem.getOffer()).thenReturn(offerWithId);
		when(order.getId()).thenReturn(ID);
		when(order.getItem()).thenReturn(offerItem);
		when(order.getTimes()).thenReturn(TIMES);
		when(order.getOrderer()).thenReturn(user);
		when(user.getId()).thenReturn(ID);
		when(offerWithId.getId()).thenReturn(ID);
		when(offerWithId.getDate()).thenReturn(DATE_EXISTING);
		when(offerWithId.isLocked()).thenReturn(Boolean.FALSE);
		when(offerWithId.getItems()).thenReturn(existingOfferItems);
		when(offerRepository.save(any(Offer.class))).thenReturn(offerWithId);
		when(offerRepository.findNextOrderByDateAsc(any(Date.class))).thenReturn(existingOffers);
		when(offerRepository.findPreviousUnlockedOrderByDateDesc(any(Date.class))).thenReturn(existingOffers);
		when(offerRepository.findByFromAndTillOrderByDate(DATE_NOT_EXISTING_START, DATE_NOT_EXISTING_END)).thenReturn(new ArrayList<Offer>());
		when(offerRepository.findByFromAndTillOrderByDate(DATE_EXISTING_START, DATE_EXISTING_END)).thenReturn(existingOffers);
		when(orderRepository.findByFromAndTillAndUserIdOrderByDate(eq(DATE_EXISTING_START), eq(DATE_EXISTING_END), any(Long.class))).thenReturn(existingOrders);
		when(mealDefinition.getDescription()).thenReturn(DESCRIPTION);
		when(mealDefinition.getHeadline()).thenReturn(HEADLINE);
		when(mealDefinition.getIngredients()).thenReturn(INGREDIENTS);
		when(mealDefinition.getPriceInCents()).thenReturn(PRICEINCENTS);
		when(mealDefinition.getType()).thenReturn(TYPE);
		when(dailyOffer.getDate()).thenReturn(DATE_DAILY_OFFER);
		when(dailyOffer.getMeals()).thenReturn(mealDefs);
		when(periodicOffer.getStartDate()).thenReturn(DATE_PERIODIC_OFFER_START);
		when(periodicOffer.getEndDate()).thenReturn(DATE_PERIODIC_OFFER_END);
		when(periodicOffer.getMeals()).thenReturn(mealDefs);
		when(offersDefinition.getDailyOffers()).thenReturn(dailyOffers);
		when(offersDefinition.getPeriodicOffers()).thenReturn(periodicOffers);
		when(parser.parse(any(File.class))).thenReturn(offersDefinition);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testGetOrCreateWithNull() {
		offerService.getOrCreateNew(null);
	}
	
	@Test
	public void testDeleteExistingWithNull() {
		offerService.deleteExisting(null);
		verify(offerRepository, never()).delete(any(Offer.class));
	}
	
	@Test
	public void testCreateNew() {
		offerService.getOrCreateNew(DATE_NOT_EXISTING);
		verify(offerRepository).findByFromAndTillOrderByDate(DATE_NOT_EXISTING_START, DATE_NOT_EXISTING_END);
		ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
		verify(offerRepository).save(captor.capture());
		assertEquals(DATE_NOT_EXISTING_EXPECTED, captor.getValue().getDate());
		verify(journalService).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testGetExisting() {
		Offer offer = offerService.getOrCreateNew(DATE_EXISTING);
		verify(offerRepository).findByFromAndTillOrderByDate(DATE_EXISTING_START, DATE_EXISTING_END);
		assertNotNull(offer);
	}
	
	@Test
	public void testLockNext() {
		offerService.lockNext();
		verify(offerRepository).findNextOrderByDateAsc(any(Date.class));
		verify(offerRepository).findPreviousUnlockedOrderByDateDesc(any(Date.class));
		verify(offerWithId).setLocked(true);
		verify(offerRepository).save(offerWithId);
		verify(journalService).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testLockOlderThan() {
		offerService.lockOlderThan(DATE_EXISTING);
		verify(offerRepository).findPreviousUnlockedOrderByDateDesc(DATE_EXISTING);
		verify(offerWithId).setLocked(true);
		verify(offerRepository).save(offerWithId);
		verify(journalService).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testGetNextAsJson() {
		JsonOffers json = offerService.getNextAsJson();
		verify(offerRepository).findNextOrderByDateAsc(any(Date.class));
		assertNotNull(json);
		assertNotNull(json.getOffers());
		assertEquals(json.getOffers().size(), 1);
		
		JsonOffer jsonOffer = json.getOffers().get(0);
		assertJsonOffer(jsonOffer);
		
		JsonOfferItemInfo jsonOfferItemInfo = jsonOffer.getItems().get(0);
		assertEquals(jsonOfferItemInfo.getId(), ID);
		assertEquals(jsonOfferItemInfo.getDescription(), DESCRIPTION);
		assertEquals(jsonOfferItemInfo.getHeadline(), HEADLINE);
		assertEquals(jsonOfferItemInfo.getIngredients(), INGREDIENTS);
		assertEquals(jsonOfferItemInfo.getMealId(), ID);
		assertEquals(jsonOfferItemInfo.getPriceInCents(), PRICEINCENTS);
		assertEquals(jsonOfferItemInfo.getName(), NAME);
		
		Map<String,JsonOfferOrder> jsonOrders = json.getOfferOrders();
		assertNotNull(jsonOrders);
		assertEquals(jsonOrders.size(), 1);
		assertTrue(jsonOrders.containsKey(IDTEXT));
		JsonOfferOrder jsonOfferOrder = jsonOrders.get(IDTEXT);
		assertJsonOfferOrder(jsonOfferOrder);
	}
	
	@Test
	public void testGetJsonOfferExisting() {
		JsonOffer json = offerService.getJsonOffer(DATE_EXISTING);
		verify(offerRepository).findByFromAndTillOrderByDate(DATE_EXISTING_START, DATE_EXISTING_END);
		assertJsonOffer(json);
	}
	
	@Test
	public void testGetJsonOfferNotExisting() {
		JsonOffer json = offerService.getJsonOffer(DATE_NOT_EXISTING);
		verify(offerRepository).findByFromAndTillOrderByDate(DATE_NOT_EXISTING_START, DATE_NOT_EXISTING_END);
		assertNotNull(json);
		assertEquals(json.getDate(), DATE_NOT_EXISTING);
		assertNull(json.getItems());
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testGetJsonOfferOrderNull() {
		offerService.getJsonOfferOrder(null);
	}
	
	@Test
	public void testGetJsonOfferOrder() {
		JsonOfferOrder json = offerService.getJsonOfferOrder(order);
		assertJsonOfferOrder(json);
	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testImportURLNull() {
		offerService.importURL(null);
	}
	
	@Test
	public void testImportURL() throws IOException {
		offerService.importURL(TEST_URL);
		verify(downloader).download(TEST_URL);
		verify(parser).parse(any(File.class));
		assertImport();
	}
	
	protected static void assertJsonOffer(JsonOffer jsonOffer) {
		assertEquals(jsonOffer.getId(), ID);
		assertEquals(jsonOffer.getDate(), DATE_EXISTING);
		assertNotNull(jsonOffer.getItems());
		assertEquals(jsonOffer.getItems().size(), 1);
	}
	
	protected static void assertJsonOfferOrder(JsonOfferOrder jsonOfferOrder) {
		assertEquals(jsonOfferOrder.getId(), ID);
		assertEquals(jsonOfferOrder.getOfferId(), ID);
		assertEquals(jsonOfferOrder.getOfferItemId(), ID);
		assertEquals(jsonOfferOrder.getTimes(), TIMES);
		assertEquals(jsonOfferOrder.getUserId(), ID);
	}
	
	protected void assertImport() {
		ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
		verify(offerRepository, times(6)).save(captor.capture());
		List<Offer> offers = captor.getAllValues();
		assertEquals(offers.get(0).getDate(), DateUtils.getWithHourOfDay(DATE_DAILY_OFFER, 12));
		assertEquals(offers.get(1).getItems().size(), 1);
		assertEquals(offers.get(2).getDate(), DateUtils.getWithHourOfDay(DATE_PERIODIC_OFFER_START, 12));
		assertEquals(offers.get(3).getItems().size(), 1);
		assertEquals(offers.get(4).getDate(), DateUtils.getWithHourOfDay(DATE_PERIODIC_OFFER_END, 12));
		assertEquals(offers.get(5).getItems().size(), 1);
		verify(offerItemService, times(3)).getOrCreate(any(Offer.class), eq(mealDefinition));
	}
	
}
