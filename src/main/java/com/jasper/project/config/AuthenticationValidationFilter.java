package com.jasper.project.config;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationValidationFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationValidationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
			String username = jwt.getClaimAsString("preferred_username");
			Object realmRoles = jwt.getClaim("realm_access");
			Object resourceRoles = jwt.getClaim("resource_access");

			System.out.println("Username: " + username);
			System.out.println("Realm Roles: " + realmRoles);
			System.out.println("Resource Roles: " + resourceRoles);

			logger.info("Username: " + username);
			logger.info("Roles: " + realmRoles);
			logger.info("Resource Roles: " + resourceRoles);
		}

		chain.doFilter(request, response);
	}
}
