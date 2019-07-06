package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonProfile;
import org.brainteam.lunchbox.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/json/profile")
public class ProfileResource {
	
	@Autowired
	private ProfileService profileService;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody JsonProfile getProfile() {
		return getProfileService().getJsonProfile();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void postProfile(@RequestBody JsonProfile profile) {
		getProfileService().updateProfile(profile);
	}
	
	protected ProfileService getProfileService() {
		return profileService;
	}

}
