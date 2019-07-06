package org.brainteam.lunchbox.mvc;import static org.mockito.Mockito.verify;import static org.mockito.Mockito.when;import static org.testng.Assert.assertSame;import org.brainteam.lunchbox.json.JsonProfile;import org.brainteam.lunchbox.services.ProfileService;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.testng.annotations.BeforeClass;import org.testng.annotations.Test;@Test(groups={"unit"})public class ProfileResourceTest {		@Mock	private JsonProfile json;		@Mock	private ProfileService profileService;	@InjectMocks	private ProfileResource profileResource;		@BeforeClass	public void setup() {		MockitoAnnotations.initMocks(this);		when(profileService.getJsonProfile()).thenReturn(json);	}		@Test	public void testGetProfile() {		JsonProfile result = profileResource.getProfile();		verify(profileService).getJsonProfile();		assertSame(result, json);	}		@Test	public void testPostProfile() {		profileResource.postProfile(json);		verify(profileService).updateProfile(json);	}	}