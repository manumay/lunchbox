package org.brainteam.lunchbox.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.brainteam.lunchbox.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityImpl implements Security {

	@Override
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return (User)principal;
		}
		return null;
	}
	
	@Override
	public boolean isLoggedIn() {
		return getCurrentUser() != null;
	}
	
	@Override
	public boolean isCurrentUserAdmin() {
		return isLoggedIn() && Boolean.TRUE.equals(getCurrentUser().getAdminRole());
	}
	
	@Override
	public boolean isCurrentUserNotAdmin() {
		return !isCurrentUserAdmin();
	}
	
	protected boolean isCurrentUserOrderer() {
		return isLoggedIn() && Boolean.TRUE.equals(getCurrentUser().getOrdererRole());
	}
	
	protected boolean isCurrentUserNotOrderer() {
		return !isCurrentUserOrderer();
	}
	
	protected boolean isCurrentUserNotAdminAndNotOrderer() {
		return isCurrentUserNotAdmin() && isCurrentUserNotOrderer();
	}
	
	@Override
	public Authentication getSecurityToken(User user) {
		return new UsernamePasswordAuthenticationToken(user, null, getAuthorities(user));
	}
	
	protected Collection<? extends GrantedAuthority> getAuthorities(User user) {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new GrantedAuthority(ROLE_USER));
		if (Boolean.TRUE.equals(user.getOrdererRole())) {
			auths.add(new GrantedAuthority(ORDERER));
		}
		if (Boolean.TRUE.equals(user.getAdminRole())) {
			auths.add(new GrantedAuthority(ADMIN));
		}
		return auths;
	}
}
