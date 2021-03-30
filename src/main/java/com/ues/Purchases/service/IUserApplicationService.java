package com.ues.Purchases.service;

import com.ues.Purchases.model.ApplicationUser;

public interface IUserApplicationService {
	
	ApplicationUser findByUsername(String username);

}
