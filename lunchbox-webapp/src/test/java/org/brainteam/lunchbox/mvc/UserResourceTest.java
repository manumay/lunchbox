package org.brainteam.lunchbox.mvc;import static org.mockito.Mockito.verify;import org.brainteam.lunchbox.json.JsonUser;import org.brainteam.lunchbox.services.UserService;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.testng.annotations.BeforeClass;import org.testng.annotations.Test;@Test(groups={"unit"})public class UserResourceTest {		private static final Long ID = Long.valueOf(1);		@Mock	private JsonUser json;	@Mock	private UserService userService;		@InjectMocks	private UserResource userResource;		@BeforeClass	public void setup() {		MockitoAnnotations.initMocks(this);	}		@Test	public void testPut() {		userResource.put(json);		verify(userService).createNew(json);	}		@Test	public void testPost() {		userResource.post(ID, json);		verify(userService).updateExisting(json);	}	//	@Test//	public void testDelete() {//		userResource.delete(ID);//		verify(userService).deleteExisting(ID);//	}	}