package org.brainteam.lunchbox.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Offer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@Column(unique=true, nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="offer", cascade=CascadeType.ALL)
	private Set<OfferItem> items;
	
	@Column(unique=false, nullable=false)
	private boolean locked;
	
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
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Set<OfferItem> getItems() {
		return items;
	}
	
	public void addToItems(OfferItem item) {
		if (items == null) {
			items = new HashSet<>();
		}
		items.add(item);
	}
	
	public void removeFromItems(OfferItem item) {
		if (items != null) {
			items.remove(item);
		}
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}
