package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonUser;
import org.brainteam.lunchbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/json/user")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void put(@RequestBody JsonUser jsonUser) {
		getUserService().createNew(jsonUser);
	}

	@RequestMapping(value="{id}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void post(@PathVariable Long id, @RequestBody JsonUser jsonUser) {
		jsonUser.setId(id);
		getUserService().updateExisting(jsonUser);
	}
	
	protected UserService getUserService() {
		return userService;
	}
	
}
