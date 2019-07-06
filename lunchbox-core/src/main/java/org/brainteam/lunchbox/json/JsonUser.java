package org.brainteam.lunchbox.json;

public class JsonUser {

	private Long id;
	private Long version;
	private String loginName;
	private String loginSecret;
	private String personnelNumber;
	private String fullName;
	private String mail;
	private Boolean ordererRole;
	private Boolean adminRole;
	private Boolean active;
	
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
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getLoginSecret() {
		return loginSecret;
	}
	
	public void setLoginSecret(String loginSecret) {
		this.loginSecret = loginSecret;
	}
	
	public String getPersonnelNumber() {
		return personnelNumber;
	}
	
	public void setPersonnelNumber(String personnelNumber) {
		this.personnelNumber = personnelNumber;
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
	
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
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
	
}
