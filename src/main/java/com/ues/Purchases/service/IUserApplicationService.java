package com.ues.Purchases.service;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.utility.NotFoundException;

public interface IUserApplicationService {
	
	ApplicationUser findByUsername(String username) throws NotFoundException;

}
