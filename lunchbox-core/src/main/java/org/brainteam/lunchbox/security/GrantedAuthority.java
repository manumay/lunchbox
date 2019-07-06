package org.brainteam.lunchbox.security;

public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	private final String authority;
	
	public GrantedAuthority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}
	
}
