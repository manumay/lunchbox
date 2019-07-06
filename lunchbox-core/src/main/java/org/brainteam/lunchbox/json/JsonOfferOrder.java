package org.brainteam.lunchbox.json;

public class JsonOfferOrder {

	private Long id;
	private Long userId;
	private Long offerId;
	private Long offerItemId;
	private Integer times;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getOfferId() {
		return offerId;
	}
	
	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}
	
	public Long getOfferItemId() {
		return offerItemId;
	}
	
	public void setOfferItemId(Long offerItemId) {
		this.offerItemId = offerItemId;
	}
	
	public Integer getTimes() {
		return times;
	}
	
	public void setTimes(Integer times) {
		this.times = times;
	}
	
}
