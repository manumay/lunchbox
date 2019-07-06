package org.brainteam.lunchbox.security;

import org.brainteam.lunchbox.domain.User;
import org.brainteam.lunchbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticationProviderImpl implements AuthenticationProvider {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Security security;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String credentials = String.valueOf(auth.getCredentials());
		String loginName = String.valueOf(auth.getPrincipal());
		
		User user = getUserService().authenticate(loginName, credentials);
		if (user != null) {
			return getSecurity().getSecurityToken(user);
		}
		
		throw new UsernameNotFoundException("no user found for " + loginName);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UsernamePasswordAuthenticationToken.class.equals(clazz);
	}
	
	protected UserService getUserService() {
		return userService;
	}
	
	protected Security getSecurity() {
		return security;
	}
	
}
