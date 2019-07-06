package org.brainteam.lunchbox.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonUser;
import org.brainteam.lunchbox.out.MailData;
import org.brainteam.lunchbox.out.MailException;
import org.brainteam.lunchbox.out.Mailer;
import org.brainteam.lunchbox.security.MD5;
import org.brainteam.lunchbox.security.Security;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups="unit")
public class UserServiceImplTest {
	
	private static final Boolean ACTIVE = Boolean.TRUE;
	private static final Boolean ADMINROLE = Boolean.FALSE;
	private static final String FULLNAME = "Harld Schmidt";
	private static final Long ID = Long.valueOf(1);
	private static final String LOGINNAME = "hschmidt";
	private static final String LOGINSECRET = "show";
	private static final String MAIL = "hschmidt@tv.de";
	private static final String PERSONNELNUMBER = "PN123";
	private static final Boolean ORDERERROLE = Boolean.TRUE;
	private static final Boolean SUPERUSER = Boolean.FALSE;
	
	@Mock
	private JsonUser jsonUserNew, jsonUserExisting;

	@Mock
	private User user1, user2, user3;
	
	@Mock
	private MailData mail;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Security security;
	
	@Mock
	private Mailer mailer;
	
	@Mock
	private JournalService journalService;
	
	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	
	private User[] users = new User[] { user2, user3 };
	
	@BeforeClass
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeMethod
	public void resetMocks() {
		reset(userRepository);
		
		when(user1.getActive()).thenReturn(ACTIVE);
		when(user1.getAdminRole()).thenReturn(ADMINROLE);
		when(user1.getFullName()).thenReturn(FULLNAME);
		when(user1.getId()).thenReturn(ID);
		when(user1.getLoginName()).thenReturn(LOGINNAME);
		when(user1.getLoginSecret()).thenReturn(MD5.hash(LOGINSECRET));
		when(user1.getMail()).thenReturn(MAIL);
		when(user1.getOrdererRole()).thenReturn(ORDERERROLE);
		when(user1.getPersonnelNumber()).thenReturn(PERSONNELNUMBER);
		when(user1.getSuperUser()).thenReturn(SUPERUSER);
		
		when(jsonUserNew.getActive()).thenReturn(ACTIVE);
		when(jsonUserNew.getAdminRole()).thenReturn(Boolean.FALSE);
		when(jsonUserNew.getFullName()).thenReturn(FULLNAME);
		when(jsonUserNew.getId()).thenReturn(null);
		when(jsonUserNew.getLoginName()).thenReturn(LOGINNAME);
		when(jsonUserNew.getLoginSecret()).thenReturn(LOGINSECRET);
		when(jsonUserNew.getMail()).thenReturn(MAIL);
		when(jsonUserNew.getPersonnelNumber()).thenReturn(PERSONNELNUMBER);
		when(jsonUserNew.getOrdererRole()).thenReturn(Boolean.TRUE);
		
		when(jsonUserExisting.getActive()).thenReturn(ACTIVE);
		when(jsonUserExisting.getAdminRole()).thenReturn(Boolean.FALSE);
		when(jsonUserExisting.getFullName()).thenReturn(FULLNAME);
		when(jsonUserExisting.getId()).thenReturn(ID);
		when(jsonUserExisting.getLoginName()).thenReturn(LOGINNAME);
		when(jsonUserExisting.getLoginSecret()).thenReturn(LOGINSECRET);
		when(jsonUserExisting.getMail()).thenReturn(MAIL);
		when(jsonUserExisting.getPersonnelNumber()).thenReturn(PERSONNELNUMBER);
		when(jsonUserExisting.getOrdererRole()).thenReturn(Boolean.TRUE);
		
		when(security.getCurrentUser()).thenReturn(user2);
		when(userRepository.findByLoginNameAndLoginSecret(eq(LOGINNAME), any(String.class))).thenReturn(user1);
		when(userRepository.findByAdminRoleTrue()).thenReturn(Arrays.asList(users));
		when(userRepository.findByActiveTrue()).thenReturn(Arrays.asList(users));
		when(userRepository.findOne(ID)).thenReturn(user1);
		when(userRepository.save(any(User.class))).thenReturn(user1);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testCreateNewNull() {
		userService.createNew(null);
	}
	
	@Test
	public void testCreateNew() {
		User user = userService.createNew(jsonUserNew);
		assertEquals(user, user1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		
		User saveUser = captor.getValue();
		assertNotNull(saveUser);
		assertEquals(saveUser.getActive(), ACTIVE);
		assertEquals(saveUser.getAdminRole(), ADMINROLE);
		assertEquals(saveUser.getFullName(), FULLNAME);
		assertNull(saveUser.getId());
		assertEquals(saveUser.getLoginName(), LOGINNAME);
		assertEquals(saveUser.getLoginSecret(), MD5.hash(LOGINSECRET));
		assertEquals(saveUser.getMail(), MAIL);
		assertEquals(saveUser.getOrdererRole(), ORDERERROLE);
		assertEquals(saveUser.getPersonnelNumber(), PERSONNELNUMBER);
		assertEquals(saveUser.getSuperUser(), SUPERUSER);
		
		verify(journalService).add(eq(UserServiceImpl.JOURNAL_USER_PUT), any(String[].class));
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testUpdateExistingNull() {
		userService.updateExisting(null);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testUpdateExistingNullId() {
		userService.updateExisting(jsonUserNew);
	}
	
	@Test
	public void testUpdateExisting() {
		User user = userService.updateExisting(jsonUserExisting);
		assertEquals(user, user1);
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		
		User saveUser = captor.getValue();
		assertNotNull(saveUser);
		assertEquals(saveUser.getActive(), ACTIVE);
		assertEquals(saveUser.getAdminRole(), ADMINROLE);
		assertEquals(saveUser.getFullName(), FULLNAME);
		assertEquals(saveUser.getId(), ID);
		assertEquals(saveUser.getLoginName(), LOGINNAME);
		assertEquals(saveUser.getLoginSecret(), MD5.hash(LOGINSECRET));
		assertEquals(saveUser.getMail(), MAIL);
		assertEquals(saveUser.getOrdererRole(), ORDERERROLE);
		assertEquals(saveUser.getPersonnelNumber(), PERSONNELNUMBER);
		assertEquals(saveUser.getSuperUser(), SUPERUSER);
		
		verify(journalService).add(eq(UserServiceImpl.JOURNAL_USER_POST), any(String[].class));
	}
	
	@Test
	public void testAuthenticate() {
		User authenticated = userService.authenticate(LOGINNAME, LOGINSECRET);
		assertNotNull(authenticated);
		assertEquals(authenticated, user1);
	}
	
	@Test
	public void testGetSuperUser() {
		when(userRepository.findByActiveTrueAndSuperUserTrue()).thenReturn(user1);
		
		User superUser = userService.getSuperUser();
		verify(userRepository).findByActiveTrueAndSuperUserTrue();
		assertEquals(superUser, user1);
	}
	
	@Test
	public void testCheckSuperUserExistsAndExists() {
		when(userRepository.findByActiveTrueAndSuperUserTrue()).thenReturn(user1);
		
		userService.checkSuperUserExists();
		verify(userRepository).findByActiveTrueAndSuperUserTrue();
		verify(userRepository, never()).save(any(User.class));
	}
	
	@Test
	public void testCheckSuperUserExistsAndNotExists() {
		when(userRepository.findByActiveTrueAndSuperUserTrue()).thenReturn(null);
		
		userService.checkSuperUserExists();
		verify(userRepository).findByActiveTrueAndSuperUserTrue();
		
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		
		User superUser = captor.getValue();
		assertTrue(superUser.getAdminRole());
		assertEquals(superUser.getFullName(), UserServiceImpl.SUPERUSER_FULLNAME);
		assertNull(superUser.getId());
		assertEquals(superUser.getLoginName(), UserServiceImpl.SUPERUSER_LOGINNAME);
		assertEquals(superUser.getLoginSecret(), MD5.hash("god"));
		assertEquals(superUser.getMail(), UserServiceImpl.SUPERUSER_MAIL);
		assertTrue(superUser.getOrdererRole());
		assertTrue(superUser.getSuperUser());
	}
	
	@Test
	public void testCurrentModifier() {
		when(userRepository.findByActiveTrueAndSuperUserTrue()).thenReturn(user1);
		
		User currentUser = userService.currentModifier();
		assertEquals(currentUser, user2);
		
		when(security.getCurrentUser()).thenReturn(null);
		currentUser = userService.currentModifier();
		assertEquals(currentUser, user1);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testSendToAdminUsersNull() throws MailException {
		userService.sendToAdminUsers(null);
	}
	
	@Test
	public void testSendToAdminUsers() throws MailException {
		userService.sendToAdminUsers(mail);
		verify(mailer).sendToUsers(mail, users);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testSendToUsersNull() throws MailException {
		userService.sendToUsers(null);
	}
	
	@Test
	public void testSendToUsers() throws MailException {
		userService.sendToUsers(mail);
		verify(mailer).sendToUsers(mail, users);
	}
	
}
