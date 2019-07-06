package org.brainteam.lunchbox.services;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonProfile;
import org.brainteam.lunchbox.security.MD5;
import org.brainteam.lunchbox.security.Security;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups="unit")
public class ProfileServiceImplTest {
	
	private static final String FULLNAME = "Rosamunde Pilcher";
	private static final Long ID = Long.valueOf(1);
	private static final String LOGINNAME = "rpilcher";
	private static final String LOGINSECRET = "schmalz";
	private static final String MAIL = "rpilcher@zdf.de";
	private static final String PERSONNELNUMBER = "ROSA123";
	private static final Long NOT_EXISTING = Long.valueOf(999);
	private static final Boolean ACTIVE = Boolean.TRUE;
	private static final Boolean ADMINROLE = Boolean.TRUE;
	private static final Boolean ORDERERROLE = Boolean.TRUE;
	private static final Boolean SUPERUSER = Boolean.TRUE;
	
	private static final String EDITED_FULLNAME = "Rosa Pilcher";
	private static final String EDITED_LOGINNAME = "rosapilcher";
	private static final String EDITED_PERSONNELNUMBER = "ROSA456";
	private static final String EDITED_LOGINSECRET = "schmalzhoch2";
	private static final String EDITED_MAIL = "rpilcher@daszweite.de";
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Security security;
	
	@Mock
	private User user;
	
	@Mock
	private JsonProfile jsonProfile, jsonProfileNotExisting;

	@InjectMocks
	private ProfileServiceImpl profileService = new ProfileServiceImpl();
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		reset(user, userRepository, security);
		
		when(userRepository.findOne(ID)).thenReturn(user);
		when(userRepository.findOne(NOT_EXISTING)).thenReturn(null);
		
		when(user.getId()).thenReturn(ID);
		when(user.getFullName()).thenReturn(FULLNAME);
		when(user.getLoginName()).thenReturn(LOGINNAME);
		when(user.getActive()).thenReturn(ACTIVE);
		when(user.getAdminRole()).thenReturn(ADMINROLE);
		when(user.getLoginSecret()).thenReturn(LOGINSECRET);
		when(user.getMail()).thenReturn(MAIL);
		when(user.getOrdererRole()).thenReturn(ORDERERROLE);
		when(user.getPersonnelNumber()).thenReturn(PERSONNELNUMBER);
		when(user.getSuperUser()).thenReturn(SUPERUSER);
		
		when(security.getCurrentUser()).thenReturn(user);
		when(security.isCurrentUserAdmin()).thenReturn(Boolean.TRUE);
		
		when(jsonProfile.getFullName()).thenReturn(EDITED_FULLNAME);
		when(jsonProfile.getId()).thenReturn(ID);
		when(jsonProfile.getLoginName()).thenReturn(EDITED_LOGINNAME);
		when(jsonProfile.getLoginSecret()).thenReturn(EDITED_LOGINSECRET);
		when(jsonProfile.getMail()).thenReturn(EDITED_MAIL);
		when(jsonProfile.getPersonnelNumber()).thenReturn(EDITED_PERSONNELNUMBER);
		
		when(jsonProfileNotExisting.getId()).thenReturn(NOT_EXISTING);
	}
	
	@Test
	public void testGetJsonProfile() {
		JsonProfile json = profileService.getJsonProfile();
		assertNotNull(json);
		assertEquals(json.getId(), ID);
		assertEquals(json.getFullName(), FULLNAME);
		assertEquals(json.getLoginName(), LOGINNAME);
		assertNull(json.getLoginSecret(), LOGINSECRET);
		assertEquals(json.getMail(), MAIL);
		assertEquals(json.getPersonnelNumber(), PERSONNELNUMBER);
	}
	
	@Test(dataProvider="trueFalse")
	public void testUpdateProfile(boolean admin) {
		when(security.isCurrentUserAdmin()).thenReturn(Boolean.valueOf(admin));
		
		profileService.updateProfile(jsonProfile);
		verify(userRepository).save(user);
		verify(user).setLoginName(EDITED_LOGINNAME);
		verify(user).setLoginSecret(MD5.hash(EDITED_LOGINSECRET));
		verify(user).setMail(EDITED_MAIL);
		if (admin) {
			verify(user).setPersonnelNumber(EDITED_PERSONNELNUMBER);
		} else {
			verify(user, never()).setPersonnelNumber(EDITED_PERSONNELNUMBER);
		}
	}
	
	@DataProvider(name="trueFalse")
	public Object[][] createData() {
		return new Object[][] { {true}, {false} } ;
	}
	
}
