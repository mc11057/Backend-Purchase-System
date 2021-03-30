package com.ues.Purchases.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.repository.IApplicationUserRepository;
import com.ues.Purchases.service.IUserApplicationService;

@Service
public class UserApplicationServiceImpl implements IUserApplicationService {

	private IApplicationUserRepository repository;

	@Autowired
	public UserApplicationServiceImpl(IApplicationUserRepository repository) {
		this.repository = repository;

	}

	@Override
	public ApplicationUser findByUsername(String username) {
		return repository.findByUsername(username);
	}

}