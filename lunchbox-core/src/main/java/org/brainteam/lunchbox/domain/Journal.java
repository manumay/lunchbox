package org.brainteam.lunchbox.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Journal {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@Column(unique=false, nullable=false)
	private String textKey;
	
	@Column(unique=false, nullable=false)
	private String params;
	
	@JoinColumn(unique=false, nullable=false)
	private User modifier;
	
	@Column(unique=false, nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;
	
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
	
	public String getTextKey() {
		return textKey;
	}
	
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	
	public String getParams() {
		return params;
	}
	
	public void setParams(String params) {
		this.params = params;
	}
	
	public User getModifier() {
		return modifier;
	}
	
	public void setModifier(User modifier) {
		this.modifier = modifier;
	}
	
	public Date getModificationDate() {
		return modificationDate;
	}
	
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	
}
