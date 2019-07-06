package org.brainteam.lunchbox.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name="ORDERING")
public class Order implements Comparable<Order> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@ManyToOne(optional=false,fetch=FetchType.EAGER)
	private User orderer;
	
	@ManyToOne(optional=true, fetch=FetchType.EAGER)
	private OfferItem item;
	
	@Column(unique=false, nullable=false)
	private Integer times;
	
	@Column(unique=false, nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderedDate;
	
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
	
	public OfferItem getItem() {
		return item;
	}
	
	public void setItem(OfferItem item) {
		this.item = item;
	}
	
	public User getOrderer() {
		return orderer;
	}
	
	public void setOrderer(User orderer) {
		this.orderer = orderer;
	}
	
	public Integer getTimes() {
		return times;
	}
	
	public void setTimes(Integer times) {
		this.times = times;
	}
	
	public Date getOrderedDate() {
		return orderedDate;
	}
	
	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}

	@Override
	public int compareTo(Order o) {
		return getItem().compareTo(o.getItem());
	}
	
}
