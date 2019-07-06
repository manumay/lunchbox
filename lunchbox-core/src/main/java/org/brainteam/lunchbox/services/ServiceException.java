package org.brainteam.lunchbox.services;

import org.brainteam.lunchbox.core.LunchboxRuntimeException;

public class ServiceException extends LunchboxRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ServiceException(Throwable t) {
		super(t);
	}

}
