package org.brainteam.lunchbox.services;

import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.json.JsonUser;
import org.brainteam.lunchbox.out.MailData;
import org.brainteam.lunchbox.out.MailException;
import org.brainteam.lunchbox.out.Mailer;
import org.brainteam.lunchbox.security.MD5;
import org.brainteam.lunchbox.security.Security;
import org.brainteam.lunchbox.util.JournalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, ApplicationListener<ApplicationEvent> {
	
	static final String SUPERUSER_FULLNAME = "Systemaccount";
	static final String SUPERUSER_LOGINNAME = "admin";
	static final String SUPERUSER_MAIL = "";
	
	static final String JOURNAL_USER_POST = "journal.user.post";
	static final String JOURNAL_USER_PUT = "journal.user.put";
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private JournalService journalService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Security security;
	
	@Override
	@Transactional(readOnly=true)
	public User authenticate(String loginName, String loginSecret) {
		return getUserRepository().findByLoginNameAndLoginSecret(loginName, MD5.hash(loginSecret));
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public User createNew(JsonUser jsonUser) {
		if (jsonUser == null) {
			throw new IllegalArgumentException("jsonUser must not be null");
		}
		
		User user = fromJsonUser(jsonUser);
		user.setId(null);
		user.setVersion(null);
		user = getUserRepository().save(user);
		getJournalService().add(JOURNAL_USER_PUT, JournalUtils.getParams(user));
		return user;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('admin')")
	public User updateExisting(JsonUser jsonUser) {
		if (jsonUser == null) {
			throw new IllegalArgumentException("jsonUser must not be null");
		}
		if (jsonUser.getId() == null) {
			throw new IllegalArgumentException("jsonUser.id must not be null");
		}
		
		User user = fromJsonUser(jsonUser);
		user = getUserRepository().save(user);
		getJournalService().add(JOURNAL_USER_POST, JournalUtils.getParams(user));
		return user;
	}
	
	protected User fromJsonUser(JsonUser jsonUser) {
		User user = new User();
		if (jsonUser.getId() != null) {
			user = getUserRepository().findOne(jsonUser.getId());
		}
		user.setLoginName(jsonUser.getLoginName());
		if (!StringUtils.isEmpty(jsonUser.getLoginSecret())) {
			user.setLoginSecret(MD5.hash(jsonUser.getLoginSecret()));
		}
		user.setPersonnelNumber(jsonUser.getPersonnelNumber());
		user.setFullName(StringUtils.trimToEmpty(jsonUser.getFullName()));
		user.setMail(StringUtils.trimToEmpty(jsonUser.getMail()));
		user.setActive(jsonUser.getActive() != null ? jsonUser.getActive() : Boolean.FALSE);
		user.setAdminRole(jsonUser.getAdminRole() != null ? jsonUser.getAdminRole() : Boolean.FALSE);
		user.setOrdererRole(jsonUser.getOrdererRole() != null ? jsonUser.getOrdererRole() : Boolean.FALSE);
		if (user.getSuperUser() == null) {
			user.setSuperUser(Boolean.FALSE);
		}
		return user;
	}
	
	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public JsonPage<JsonUser> getUsersJson(int page, int size, String sort, String order) {
		JsonPage<JsonUser> json = new JsonPage<>();
		PageRequest pr = new PageRequest(page, size, new Sort(new Order(Direction.fromString(order), sort)));
		Page<User> users = getUserRepository().findAll(pr);
		for (User user : users) {
			JsonUser jsonUser = toJsonUser(user);
			json.addToItems(jsonUser);
		}
		json.setPage(users.getNumber() + 1);
		json.setSize(users.getSize());
		json.setNumberOfItems(users.getNumberOfElements());
		json.setSort(sort);
		json.setOrder(order);
		json.setTotalPages(users.getTotalPages());
		json.setTotalItems(users.getTotalElements());
		return json;
	}
	
	protected JsonUser toJsonUser(User user) {
		JsonUser jsonUser = new JsonUser();
		jsonUser.setId(user.getId());
		jsonUser.setVersion(user.getVersion());
		jsonUser.setLoginName(user.getLoginName());
		jsonUser.setFullName(user.getFullName());
		jsonUser.setMail(user.getMail());
		jsonUser.setActive(user.getActive());
		jsonUser.setAdminRole(user.getAdminRole());
		jsonUser.setOrdererRole(user.getOrdererRole());
		if (getSecurity().isCurrentUserAdmin()) {
			jsonUser.setPersonnelNumber(user.getPersonnelNumber());
		}
		return jsonUser;
	}
	
	@Override
	public User currentModifier() {
		User currentUser = getSecurity().getCurrentUser();
		return currentUser != null ? currentUser : getSuperUser();
	}

	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public void sendToAdminUsers(MailData mail) throws MailException {
		if (mail == null) {
			throw new IllegalArgumentException("mail must not be null");
		}
		User[] admins = getUserRepository().findByAdminRoleTrue().toArray(new User[0]);
		getMailer().sendToUsers(mail, admins);
	}

	@Override
	@Transactional(readOnly=true)
	@PreAuthorize("hasRole('admin')")
	public void sendToUsers(MailData mail) throws MailException {
		if (mail == null) {
			throw new IllegalArgumentException("mail must not be null");
		}
		User[] admins = getUserRepository().findAll().toArray(new User[0]);
		getMailer().sendToUsers(mail, admins);
	}
	
	@Override
	@Transactional(readOnly=true)
	public User getSuperUser() {
		return getUserRepository().findByActiveTrueAndSuperUserTrue();
	}
	
	@Override
	@Transactional
	public void checkSuperUserExists() {
		User user = getSuperUser();
		if (user == null) {
			User superUser = new User();
			superUser.setActive(Boolean.TRUE);
			superUser.setAdminRole(Boolean.TRUE);
			superUser.setFullName(SUPERUSER_FULLNAME);
			superUser.setLoginSecret(MD5.hash("password"));
			superUser.setMail(SUPERUSER_MAIL);
			superUser.setLoginName(SUPERUSER_LOGINNAME);
			superUser.setOrdererRole(Boolean.TRUE);
			superUser.setSuperUser(Boolean.TRUE);
			getUserRepository().save(superUser);
		}
	}
	
	@Override
	@Transactional
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			checkSuperUserExists();
		}
	}
	
	protected Mailer getMailer() {
		return mailer;
	}
	
	protected UserRepository getUserRepository() {
		return userRepository;
	}

	protected JournalService getJournalService() {
		return journalService;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
}
