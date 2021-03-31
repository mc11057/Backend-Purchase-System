package com.ues.Purchases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.service.IUserApplicationService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserApplicationService service;


	public UserController(IUserApplicationService service) {
		this.service = service;
	}

	
	
	@PostMapping("/loadUserByUserName")
	public ResponseEntity<ApplicationUser> loadUserByUserName(@RequestBody String username) {
		
		ApplicationUser user = new ApplicationUser();
		try {
			 user = service.findByUsername(username);
			return new ResponseEntity<ApplicationUser>(user, HttpStatus.OK);
			
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
