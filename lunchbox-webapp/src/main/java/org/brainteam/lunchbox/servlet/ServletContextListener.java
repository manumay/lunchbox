package org.brainteam.lunchbox.servlet;

import javax.servlet.ServletContextEvent;

import org.brainteam.lunchbox.core.DirectoriesImpl;
import org.brainteam.lunchbox.core.Version;
import org.brainteam.lunchbox.util.WebUtils;
import org.springframework.web.context.ContextLoaderListener;

public class ServletContextListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent e) {
		Version.set(WebUtils.getVersion(e.getServletContext()));
		updateDerbyHomePath();
		super.contextInitialized(e);
	}

	private void updateDerbyHomePath() {
		System.setProperty("derby.system.home", DirectoriesImpl.getDatabaseDir().getAbsolutePath());
	}
	
}
