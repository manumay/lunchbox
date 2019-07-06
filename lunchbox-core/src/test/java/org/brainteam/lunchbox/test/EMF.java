package org.brainteam.lunchbox.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {

	private static EntityManagerFactory INSTANCE;
	
	static {
		try {
			INSTANCE = Persistence.createEntityManagerFactory("lunchbox");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EntityManagerFactory get() {
		return INSTANCE;
	}
	
	private EMF() {
	}
	
}
