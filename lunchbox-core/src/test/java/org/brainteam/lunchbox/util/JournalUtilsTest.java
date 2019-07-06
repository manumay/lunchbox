package org.brainteam.lunchbox.util;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups={"unit"})
public class JournalUtilsTest {
	
	private static final Long ID = Long.valueOf(1);
	private static final String IDTEXT = String.valueOf(ID);
	private static final String HEADLINE = "headline";
	private static final Date DATE = new Date();
	private static final String DATETEXT = new SimpleDateFormat(JournalUtils.DATE_FORMAT).format(DATE);
	private static final String NAME = "name";
	private static final String FULLNAME = "fullname";
	private static final String LOGINNAME = "loginname";
	private static final String TIMESTEXT = "0";
	
	@Mock
	private Meal meal;
	
	@Mock
	private Offer offer;
	
	@Mock
	private OfferItem offerItem;
	
	@Mock
	private Order order;
	
	@Mock
	private User user;
	
	@BeforeClass
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(meal.getId()).thenReturn(ID);
		when(meal.getHeadline()).thenReturn(HEADLINE);
		when(offer.getId()).thenReturn(ID);
		when(offer.getDate()).thenReturn(DATE);
		when(offerItem.getId()).thenReturn(ID);
		when(offerItem.getOffer()).thenReturn(offer);
		when(offerItem.getMeal()).thenReturn(meal);
		when(offerItem.getName()).thenReturn(NAME);
		when(order.getId()).thenReturn(ID);
		when(order.getItem()).thenReturn(offerItem);
		when(order.getOrderer()).thenReturn(user);
		when(user.getId()).thenReturn(ID);
		when(user.getFullName()).thenReturn(FULLNAME);
		when(user.getLoginName()).thenReturn(LOGINNAME);
	}

	@Test
	public void testMeal() {
		String[] params = JournalUtils.getParams(meal);
		assertNotNull(params);
		assertEquals(params, new String[] { IDTEXT, HEADLINE } );
	}
	
	@Test
	public void testOffer() {
		String[] params = JournalUtils.getParams(offer);
		assertNotNull(params);
		assertEquals(params, new String[] { IDTEXT, DATETEXT } );
	}
	
	@Test
	public void testOfferItem() {
		String[] params = JournalUtils.getParams(offerItem);
		assertNotNull(params);
		assertEquals(params, new String[] { IDTEXT, DATETEXT, NAME, HEADLINE } );
	}
	
	@Test
	public void testOrder() {
		String[] params = JournalUtils.getParams(order);
		assertNotNull(params);
		assertEquals(params, new String[] { IDTEXT, TIMESTEXT, HEADLINE, FULLNAME, DATETEXT } );
	}
	
	@Test
	public void testUser() {
		String[] params = JournalUtils.getParams(user);
		assertNotNull(params);
		assertEquals(params, new String[] { IDTEXT, LOGINNAME } );
	}
	
}
