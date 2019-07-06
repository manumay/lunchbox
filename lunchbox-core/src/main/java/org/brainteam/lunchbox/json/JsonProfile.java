package org.brainteam.lunchbox.json;

public class JsonProfile {

	private Long id;
	private String loginName;
	private String fullName;
	private String mail;
	private String loginSecret;
	private String personnelNumber;
	private boolean ordererRole;
	private boolean adminRole;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public boolean isAdminRole() {
		return adminRole;
	}
	
	public void setAdminRole(boolean adminRole) {
		this.adminRole = adminRole;
	}
	
	public boolean isOrdererRole() {
		return ordererRole;
	}
	
	public void setOrdererRole(boolean ordererRole) {
		this.ordererRole = ordererRole;
	}
	
}
