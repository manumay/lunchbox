package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferRepository;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonOption;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups="unit")
public class OptionServiceImplTest {
	
	private static final Long ID1 = Long.valueOf(1);
	private static final Long ID2 = Long.valueOf(2);
	private static final String MEAL1_HEADLINE = "Spaghetti";
	private static final String MEAL1_DESCRIPTION = "mit Tomatensoße";
	private static final String MEAL1_EXPECTED_VALUE = MEAL1_HEADLINE + " " + MEAL1_DESCRIPTION;
	private static final String MEAL2_HEADLINE = "Schnitzel";
	private static final String MEAL2_DESCRIPTION = "mit Pommes";
	private static final String MEAL2_EXPECTED_VALUE = MEAL2_HEADLINE + " " + MEAL2_DESCRIPTION;
	private static final String OFFERITEM1_NAME = "Menü 1";
	private static final String OFFERITEM1_EXPECTED_VALUE = OFFERITEM1_NAME + ": " + MEAL1_HEADLINE;
	private static final String OFFERITEM2_NAME = "Menü 2";
	private static final String OFFERITEM2_EXPECTED_VALUE = OFFERITEM2_NAME + ": " + MEAL2_HEADLINE;
	private static final String USER1_FULLNAME = "Sylvester Stalone";
	private static final String USER2_FULLNAME = "Arnold Schwarzenegger";
	
	private List<Meal> meals = new ArrayList<>();
	private Set<OfferItem> offerItems = new LinkedHashSet<>();
	private List<User> users = new ArrayList<>();
	
	@Mock
	private MealRepository mealRepository;
	
	@Mock
	private OfferRepository offerRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Meal meal1;
	
	@Mock
	private Meal meal2;
	
	@Mock
	private Offer offer;
	
	@Mock
	private OfferItem offerItem1;
	
	@Mock
	private OfferItem offerItem2;
	
	@Mock
	private User user1;
	
	@Mock
	private User user2;
	
	@InjectMocks
	private OptionServiceImpl optionService = new OptionServiceImpl();
	
	@BeforeClass
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		meals.add(meal1);
		meals.add(meal2);
		
		when(meal1.getId()).thenReturn(ID1);
		when(meal1.getHeadline()).thenReturn(MEAL1_HEADLINE);
		when(meal1.getDescription()).thenReturn(MEAL1_DESCRIPTION);
		when(meal2.getId()).thenReturn(ID2);
		when(meal2.getHeadline()).thenReturn(MEAL2_HEADLINE);
		when(meal2.getDescription()).thenReturn(MEAL2_DESCRIPTION);
		when(mealRepository.findAll()).thenReturn(meals);
		
		offerItems.add(offerItem1);
		offerItems.add(offerItem2);
		
		when(offerItem1.getId()).thenReturn(ID1);
		when(offerItem1.getName()).thenReturn(OFFERITEM1_NAME);
		when(offerItem1.getMeal()).thenReturn(meal1);
		when(offerItem2.getId()).thenReturn(ID2);
		when(offerItem2.getName()).thenReturn(OFFERITEM2_NAME);
		when(offerItem2.getMeal()).thenReturn(meal2);
		when(offer.getItems()).thenReturn(offerItems);
		when(offerRepository.findByFromAndTillOrderByDate(any(Date.class), any(Date.class))).thenReturn(Collections.singletonList(offer));
		
		users.add(user1);
		users.add(user2);
		
		when(user1.getId()).thenReturn(ID1);
		when(user1.getFullName()).thenReturn(USER1_FULLNAME);
		when(user2.getId()).thenReturn(ID2);
		when(user2.getFullName()).thenReturn(USER2_FULLNAME);
		when(userRepository.findByActiveTrue()).thenReturn(users);
	}
	
	@Test
	public void testMealOptions() {
		List<JsonOption> options = optionService.getMeals();
		assertNotNull(options);
		assertEquals(options.size(), 2);
		assertEquals(options.get(0).getValue(), ID1);
		assertEquals(options.get(0).getText(), MEAL1_EXPECTED_VALUE);
		assertEquals(options.get(1).getValue(), ID2);
		assertEquals(options.get(1).getText(), MEAL2_EXPECTED_VALUE);
	}
	
	@Test
	public void testOfferItemOptions() {
		List<JsonOption> options = optionService.getOfferItems(new Date());
		assertNotNull(options);
		assertEquals(options.size(), 2);
		assertEquals(options.get(0).getValue(), ID1);
		assertEquals(options.get(0).getText(), OFFERITEM1_EXPECTED_VALUE);
		assertEquals(options.get(1).getValue(), ID2);
		assertEquals(options.get(1).getText(), OFFERITEM2_EXPECTED_VALUE);
	}
	
	@Test
	public void testUserOptions() {
		List<JsonOption> options = optionService.getUsers();
		assertNotNull(options);
		assertEquals(options.size(), 2);
		assertEquals(options.get(0).getValue(), ID1);
		assertEquals(options.get(0).getText(), USER1_FULLNAME);
		assertEquals(options.get(1).getValue(), ID2);
		assertEquals(options.get(1).getText(), USER2_FULLNAME);
	}
	
}
