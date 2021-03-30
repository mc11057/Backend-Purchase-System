package com.ues.Purchases.security;

import static com.ues.Purchases.security.SecurityConstants.HEADER_STRING;
import static com.ues.Purchases.security.SecurityConstants.SECRET;
import static com.ues.Purchases.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.service.impl.UserDetailsServiceImpl;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private UserDetailsServiceImpl userDetailsService;
	private static final String prefixRole = "ROLE_";

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
			UserDetailsServiceImpl userDetailsService) {
		super(authenticationManager);
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String userName = getUserNameFromToken(token);
			if (!ObjectUtils.isEmpty(userName)) {
				ApplicationUser user = getUserFromDatabase(userName);
				if (user != null) {
					List<GrantedAuthority> roleList = new ArrayList<GrantedAuthority>();
					getRolesFromUser(user, roleList);
					return new UsernamePasswordAuthenticationToken(userName, null, roleList);

				}
			}
		}
		return null;
	}

	private List<GrantedAuthority> getRolesFromUser(ApplicationUser user, List<GrantedAuthority> roleList) {
		user.getRoles()
		.forEach(userRole -> roleList.add(new SimpleGrantedAuthority(prefixRole + userRole.getRole().getName().toString().toUpperCase())));
		return roleList;
	}

	private ApplicationUser getUserFromDatabase(String userName) {
		return userDetailsService.findByUserName(userName);
	}

	private String getUserNameFromToken(String token) {

		try {
			return JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, ""))
					.getSubject();
		} catch (Exception e) {
			return null;
		}
	}
}
