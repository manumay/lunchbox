package org.brainteam.lunchbox.mvc;

import org.brainteam.lunchbox.cmd.Command;
import org.brainteam.lunchbox.cmd.CommandException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("action/trigger")
public class ActionController {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void triggerJob(@RequestParam String name) {
		Object action = getApplicationContext().getBean(name);
		if (action instanceof Command) {
			try {
				((Command) action).execute();
			} catch (CommandException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
