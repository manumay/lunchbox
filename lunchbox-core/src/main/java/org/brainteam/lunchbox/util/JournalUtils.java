package org.brainteam.lunchbox.util;

import java.text.SimpleDateFormat;

import org.brainteam.lunchbox.domain.Meal;
import org.brainteam.lunchbox.domain.Offer;
import org.brainteam.lunchbox.domain.OfferItem;
import org.brainteam.lunchbox.domain.Order;
import org.brainteam.lunchbox.domain.User;

public final class JournalUtils {
	
	static final String DATE_FORMAT = "d. MMMM yyyy";

	private JournalUtils() {
	}
	
	public static String[] getParams(Meal meal) {
		String[] params = new String[2];
		params[0] = String.valueOf(meal.getId());
		params[1] = meal.getHeadline();
		return params;
	}
	
	public static String[] getParams(Offer offer) {
		String[] params = new String[2];
		params[0] = String.valueOf(offer.getId());
		params[1] = new SimpleDateFormat(DATE_FORMAT).format(offer.getDate());
		return params;
	}
	
	public static String[] getParams(OfferItem offerItem) {
		String[] params = new String[4];
		params[0] = String.valueOf(offerItem.getId());
		params[1] = new SimpleDateFormat(DATE_FORMAT).format(offerItem.getOffer().getDate());
		params[2] = offerItem.getName();
		params[3] = offerItem.getMeal().getHeadline();
		return params;
	}
	
	public static String[] getParams(Order order) {
		String[] params = new String[5];
		params[0] = String.valueOf(order.getId());
		params[1] = String.valueOf(order.getTimes());
		params[2] = order.getItem().getMeal().getHeadline();
		params[3] = order.getOrderer().getFullName();
		params[4] = new SimpleDateFormat(DATE_FORMAT).format(order.getItem().getOffer().getDate());
		return params;
	}
	
	public static String[] getParams(User user) {
		String[] params = new String[2];
		params[0] = String.valueOf(user.getId());
		params[1] = user.getLoginName();
		return params;
	}
	
}
