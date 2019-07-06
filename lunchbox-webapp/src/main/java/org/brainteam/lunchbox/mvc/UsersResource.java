package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.json.JsonPage;
import org.brainteam.lunchbox.json.JsonUser;
import org.brainteam.lunchbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/users")
public class UsersResource {

	@Autowired
	private UserService userService;
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody JsonPage<JsonUser> get(@RequestParam int page, @RequestParam int size,
			@RequestParam String sort, @RequestParam String order) {
		return getUserService().getUsersJson(page - 1, size, sort, order);
	}
	
	protected UserService getUserService() {
		return userService;
	}
	
}
