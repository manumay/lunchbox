package org.brainteam.lunchbox.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class OfferItem implements Comparable<OfferItem> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@Column(unique=false, nullable=false)
	private String name;
	
	@ManyToOne(optional=false)
	private Offer offer;
	
	@ManyToOne(optional=false, cascade=CascadeType.PERSIST)
	private Meal meal;
	
	@Column(unique=false, nullable=false)
	private Integer priceInCents;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Offer getOffer() {
		return offer;
	}
	
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	public Meal getMeal() {
		return meal;
	}
	
	public void setMeal(Meal meal) {
		this.meal = meal;
	}
	
	public Integer getPriceInCents() {
		return priceInCents;
	}
	
	public void setPriceInCents(Integer priceInCents) {
		this.priceInCents = priceInCents;
	}
	
	@Override
	public int compareTo(OfferItem o) {
		return this.getName().compareTo(o.getName());
	}
	
}
