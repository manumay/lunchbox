package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.brainteam.lunchbox.dao.MealRepository;
import org.brainteam.lunchbox.dao.OfferItemRepository;
import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.json.JsonOfferItem;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups={"unit"})
public class OfferItemServiceImplTest {
	
	private static final Date DATE = new Date();
	private static final Long ID = Long.valueOf(9);
	private static final String NAME = "name";
	private static final Integer PRICEINCENTS = Integer.valueOf(100);
	
	@Mock
	private JournalService journalService;

	@Mock
	private OfferItemRepository offerItemRepository;
	
	@Mock
	private OfferService offerService;
	
	@Mock
	private MealRepository mealRepository;
	
	@Mock
	private Offer offer;
	
	@Mock
	private Meal meal;
	
	@Mock
	private OfferItem offerItemWithId;
	
	@Mock
	private JsonOfferItem jsonOfferItemToCreate;
	
	@Mock
	private JsonOfferItem jsonOfferItemToUpdate;
	
	@InjectMocks
	private OfferItemServiceImpl offerItemService = new OfferItemServiceImpl();
	
	@BeforeMethod
    public void init() {
		MockitoAnnotations.initMocks(this);
		when(meal.getId()).thenReturn(ID);
		when(offer.getId()).thenReturn(ID);
		when(offer.getDate()).thenReturn(DATE);
		when(offerItemWithId.getId()).thenReturn(ID);
		when(offerItemWithId.getName()).thenReturn(NAME);
		when(offerItemWithId.getPriceInCents()).thenReturn(PRICEINCENTS);
		when(offerItemWithId.getMeal()).thenReturn(meal);
		when(offerItemWithId.getOffer()).thenReturn(offer);
		when(jsonOfferItemToCreate.getMealId()).thenReturn(ID);
		when(jsonOfferItemToCreate.getOfferDate()).thenReturn(DATE);
		when(jsonOfferItemToCreate.getPriceInCents()).thenReturn(PRICEINCENTS);
		when(jsonOfferItemToCreate.getName()).thenReturn(NAME);
		when(jsonOfferItemToUpdate.getId()).thenReturn(ID);
		when(jsonOfferItemToUpdate.getMealId()).thenReturn(ID);
		when(jsonOfferItemToUpdate.getOfferDate()).thenReturn(DATE);
		when(jsonOfferItemToUpdate.getPriceInCents()).thenReturn(PRICEINCENTS);
		when(jsonOfferItemToUpdate.getName()).thenReturn(NAME);
		when(offerService.getOrCreateNew(DATE)).thenReturn(offer);
		when(mealRepository.findOne(ID)).thenReturn(meal);
		when(offerItemRepository.findOne(ID)).thenReturn(offerItemWithId);
		when(offerItemRepository.save(any(OfferItem.class))).thenReturn(offerItemWithId);
    }
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testCreateNewWithNull() {
		offerItemService.createNew((JsonOfferItem)null);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testUpdateExistingWithNull() {
		offerItemService.updateExisting((JsonOfferItem)null);
	}
	
	@Test
	public void testDeleteExistingWithNull() {
		offerItemService.deleteExisting(null); // nothing should happen
		verify(offerItemRepository, never()).delete(any(Long.class));
	}
	
	@Test
	public void testCreateNew() {
		offerItemService.createNew(jsonOfferItemToCreate);
		ArgumentCaptor<OfferItem> captor = ArgumentCaptor.forClass(OfferItem.class);
		verify(offerItemRepository).save(captor.capture());
		assertEquals(jsonOfferItemToCreate.getName(), captor.getValue().getName());
		assertEquals(jsonOfferItemToCreate.getPriceInCents(), captor.getValue().getPriceInCents());
		verify(mealRepository).findOne(ID);
		assertEquals(jsonOfferItemToCreate.getMealId(), captor.getValue().getMeal().getId());
		verify(offerService).getOrCreateNew(DATE);
		assertEquals(jsonOfferItemToCreate.getOfferDate(), captor.getValue().getOffer().getDate());
		verify(journalService).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testUpdateExisting() {
		offerItemService.updateExisting(jsonOfferItemToUpdate);
		ArgumentCaptor<OfferItem> captor = ArgumentCaptor.forClass(OfferItem.class);
		verify(offerItemRepository).save(captor.capture());
		assertEquals(captor.getValue().getName(), jsonOfferItemToUpdate.getName());
		assertEquals(captor.getValue().getPriceInCents(), jsonOfferItemToUpdate.getPriceInCents());
		verify(mealRepository).findOne(ID);
		assertEquals(captor.getValue().getMeal().getId(), jsonOfferItemToUpdate.getMealId());
		verify(offerService).getOrCreateNew(DATE);
		assertEquals(captor.getValue().getOffer().getDate(), jsonOfferItemToUpdate.getOfferDate());
		verify(journalService).add(any(String.class), any(String[].class));
	}
	
	@Test
	public void testDeleteExisting() {
		offerItemService.deleteExisting(ID);
		verify(offerItemRepository).findOne(ID);
		verify(offerItemRepository).delete(offerItemWithId);
		verify(journalService).add(any(String.class), any(String[].class));
	}

}
