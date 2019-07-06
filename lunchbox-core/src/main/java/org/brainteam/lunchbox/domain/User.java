package org.brainteam.lunchbox.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="USERS")
public class User implements Comparable<User> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column(unique=false, nullable=true)
	private Long version;
	
	@Column(unique=true, nullable=false, length=50)
	private String loginName;
	
	@Column(unique=false, nullable=false)
	private String fullName;
	
	@Column(unique=false, nullable=false)
	private String mail;
	
	@Column(unique=false, nullable=true)
	private String personnelNumber;
	
	@Column(unique=false, nullable=false, length=50)
	private String loginSecret;
	
	@Column(unique=false, nullable=false)
	private Boolean adminRole;
	
	@Column(unique=false, nullable=false)
	private Boolean ordererRole;
	
	@Column(unique=false, nullable=false)
	private Boolean active;
	
	@Column(unique=false, nullable=false)
	private Boolean superUser;
	
	@Column(unique=false, nullable=false)
	private boolean deleted;
	
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
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String shortName) {
		this.loginName = shortName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getPersonnelNumber() {
		return personnelNumber;
	}
	
	public void setPersonnelNumber(String personnelNumber) {
		this.personnelNumber = personnelNumber;
	}
	
	public String getLoginSecret() {
		return loginSecret;
	}
	
	public void setLoginSecret(String loginSecret) {
		this.loginSecret = loginSecret;
	}
	
	public Boolean getAdminRole() {
		return adminRole;
	}
	
	public void setAdminRole(Boolean adminRole) {
		this.adminRole = adminRole;
	}
	
	public Boolean getOrdererRole() {
		return ordererRole;
	}
	
	public void setOrdererRole(Boolean ordererRole) {
		this.ordererRole = ordererRole;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Boolean getSuperUser() {
		return superUser;
	}
	
	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public int compareTo(User u) {
		return getFullName().compareTo(u.getFullName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return ((User)obj).getId().equals(getId());
		}
		return false;
	}
	
}
