package com.ues.Purchases.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.Purchases.model.Ciudad;
import com.ues.Purchases.service.ICiudadService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/ciudad")
public class CiudadController {


	@Autowired
	private ICiudadService ciudadService;

	public CiudadController(ICiudadService ciudadService) {
		super();
		this.ciudadService = ciudadService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Ciudad>> getCiudad() {

		List<Ciudad> ciudades = new ArrayList<Ciudad>();
		try {
			ciudades = (List<Ciudad>) ciudadService.findAllCiudad();
			return new ResponseEntity<List<Ciudad>>(ciudades, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Ciudad> get(@PathVariable("id") Long id) {
		try {
			Ciudad ciudades = ciudadService.findById(id);
			return new ResponseEntity<Ciudad>(ciudades, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	
	
}
