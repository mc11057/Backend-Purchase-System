package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ues.Purchases.model.ApplicationUser;

public interface IApplicationUserRepository  extends JpaRepository<ApplicationUser, Long> {
	
	ApplicationUser findByUsername(String username);
}