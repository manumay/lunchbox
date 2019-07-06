package org.brainteam.lunchbox.mvc;import static org.mockito.Mockito.verify;import static org.mockito.Mockito.when;import static org.testng.Assert.assertSame;import org.brainteam.lunchbox.domain.Order;import org.brainteam.lunchbox.json.JsonOfferOrder;import org.brainteam.lunchbox.json.JsonOrder;import org.brainteam.lunchbox.services.OfferService;import org.brainteam.lunchbox.services.OrderService;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.testng.annotations.BeforeClass;import org.testng.annotations.Test;@Test(groups={"unit"})public class OrderResourceTest {		private static final Long ID = Long.valueOf(1);		@Mock	private Order order;	@Mock	private JsonOfferOrder json, jsonNew;		@Mock	private JsonOrder jsonOrder;		@Mock	private OrderService orderService;		@Mock	private OfferService offerService;		@InjectMocks	private OrderResource orderResource;		@BeforeClass	public void setup() {		MockitoAnnotations.initMocks(this);		when(orderService.createNew(json)).thenReturn(order);		when(orderService.getJsonOrder(ID)).thenReturn(jsonOrder);		when(offerService.getJsonOfferOrder(order)).thenReturn(jsonNew);	}		@Test	public void testGet() {		JsonOrder result = orderResource.get(ID);		verify(orderService).getJsonOrder(ID);		assertSame(result, jsonOrder);	}		@Test	public void testPut() {		JsonOfferOrder result = orderResource.put(json);		verify(orderService).createNew(json);		assertSame(result, jsonNew);	}		@Test	public void testPost() {		orderResource.post(ID, json);		verify(orderService).updateExisting(json);	}		@Test	public void testDelete() {		orderResource.delete(ID);		verify(orderService).deleteExisting(ID);	}}