package com.ues.Purchases.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.repository.IApplicationUserRepository;
import com.ues.Purchases.service.IUserApplicationService;
import com.ues.Purchases.utility.NotFoundException;

import static java.util.Collections.emptyList;


@Service
public class UserDetailsServiceImpl implements UserDetailsService,IUserApplicationService {

	
	@Autowired
	private IApplicationUserRepository repository;

	public UserDetailsServiceImpl(IApplicationUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = repository.findByUsername(username);
		
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
	}

	@Override
	public ApplicationUser findByUsername(String username) throws NotFoundException {
		ApplicationUser user = repository.findByUsername(username);
		if(user == null) throw new NotFoundException("User: " + username + "not found in the database");
		user.setPassword(null);//don't expose the password to clients
		return user;
	}

	
	

}