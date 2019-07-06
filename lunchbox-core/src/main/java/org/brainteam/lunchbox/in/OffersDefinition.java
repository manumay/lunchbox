package org.brainteam.lunchbox.in;

import java.util.ArrayList;
import java.util.List;

public class OffersDefinition {

	private List<OfferDailyDefinition> dailyOffers = new ArrayList<>();
	private List<OfferPeriodicDefinition> periodicOffers = new ArrayList<>();
	
	public List<OfferDailyDefinition> getDailyOffers() {
		return dailyOffers;
	}
	
	public void addDailyOffer(OfferDailyDefinition dailyOffer) {
		if (!dailyOffers.contains(dailyOffer)) {
			dailyOffers.add(dailyOffer);
		}
	}
	
	public List<OfferPeriodicDefinition> getPeriodicOffers() {
		return periodicOffers;
	}
	
	public void addPeriodicOffer(OfferPeriodicDefinition periodicOffer) {
		if (!periodicOffers.contains(periodicOffer)) {
			periodicOffers.add(periodicOffer);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dailyOffers == null) ? 0 : dailyOffers.hashCode());
		result = prime * result + ((periodicOffers == null) ? 0 : periodicOffers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		OffersDefinition other = (OffersDefinition) obj;
		if (dailyOffers == null) {
			if (other.dailyOffers != null) {
				return false;
			}
		} else if (!dailyOffers.equals(other.dailyOffers)) {
			return false;
		}
		
		if (periodicOffers == null) {
			if (other.periodicOffers != null) {
				return false;
			}
		} else if (!periodicOffers.equals(other.periodicOffers)) {
			return false;
		}
		
		return true;
	}
	
}
