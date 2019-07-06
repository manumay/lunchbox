package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.json.JsonUser;
import org.brainteam.lunchbox.out.MailData;
import org.brainteam.lunchbox.out.MailException;

public interface UserService {
	
	User authenticate(String loginName, String credentials);
	
	User createNew(JsonUser user);
	
	User updateExisting(JsonUser jsonUser);
	
	JsonPage<JsonUser> getUsersJson(int pageNumber, int pageSize, String sort, String sortOrder);
	
	User currentModifier();

	void sendToAdminUsers(MailData mail) throws MailException;
	
	void sendToUsers(MailData mail) throws MailException;
	
	User getSuperUser();
	
	void checkSuperUserExists();
	
}
