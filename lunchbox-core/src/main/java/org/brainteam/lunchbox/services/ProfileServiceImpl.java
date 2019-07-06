package org.brainteam.lunchbox.services;

import org.apache.commons.lang.StringUtils;
import org.brainteam.lunchbox.core.LunchboxRuntimeException;
import org.brainteam.lunchbox.dao.UserRepository;
import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonProfile;
import org.brainteam.lunchbox.security.MD5;
import org.brainteam.lunchbox.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Security security;
	
	@Override
	@PreAuthorize("hasRole('user')")
	public JsonProfile getJsonProfile() {
		User user = getSecurity().getCurrentUser();
		return toJsonProfile(user);
	}
	
	protected JsonProfile toJsonProfile(User user) {
		JsonProfile json = new JsonProfile();
		json.setAdminRole(user.getAdminRole());
		json.setFullName(user.getFullName());
		json.setId(user.getId());
		json.setLoginName(user.getLoginName());
		// json.setLoginSecret(user.getLoginSecret());
		json.setMail(user.getMail());
		json.setOrdererRole(user.getOrdererRole());
		json.setPersonnelNumber(user.getPersonnelNumber());
		return json;
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('user')")
	public void updateProfile(JsonProfile profile) {
		User user = fromJsonProfile(profile);
		getUserRepository().save(user);
	}
	
	protected User fromJsonProfile(JsonProfile profile) {
		User user = getUserRepository().findOne(profile.getId());
		if (user == null) {
			throw new LunchboxRuntimeException("user with id " + profile.getId() + " does not exist");
		}
		
		String fullName = profile.getFullName();
		if (!StringUtils.isEmpty(fullName)) {
			user.setFullName(fullName);
		}
		
		String loginName = profile.getLoginName();
		if (!StringUtils.isEmpty(loginName)) {
			user.setLoginName(loginName);
		}
		
		String loginSecret = profile.getLoginSecret();
		if (!StringUtils.isEmpty(loginSecret)) {
			user.setLoginSecret(MD5.hash(loginSecret));
		}
		
		String mail = profile.getMail();
		if (!StringUtils.isEmpty(mail)) {
			user.setMail(mail);
		}
		
		if (getSecurity().isCurrentUserAdmin()) {
			String personnelNumber = profile.getPersonnelNumber();
			user.setPersonnelNumber(personnelNumber);
		}
		
		return user;
	}
	
	protected UserRepository getUserRepository() {
		return userRepository;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
}
