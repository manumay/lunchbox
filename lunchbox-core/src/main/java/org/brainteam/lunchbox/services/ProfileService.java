package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.json.JsonProfile;

public interface ProfileService {

	JsonProfile getJsonProfile();
	
	void updateProfile(JsonProfile profile);
	
}
