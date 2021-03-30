package com.ues.Purchases.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.repository.IApplicationUserRepository;
import static java.util.Collections.emptyList;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private IApplicationUserRepository repository;

	public UserDetailsServiceImpl(IApplicationUserRepository applicationUserRepository) {
		this.repository = applicationUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = repository.findByUsername(username);
		
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
	}

	public ApplicationUser findByUserName(String username) {
		return repository.findByUsername(username);
	}

}