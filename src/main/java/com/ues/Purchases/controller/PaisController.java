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

import com.ues.Purchases.model.Pais;
import com.ues.Purchases.model.Region;
import com.ues.Purchases.service.IPaisService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/pais")
public class PaisController {

	@Autowired
	private IPaisService paisService;
	
	
	
	public PaisController(IPaisService paisService ) {
		this.paisService = paisService;

	}
	
	
	@GetMapping()
	public ResponseEntity<List<Pais>> getPais() {

		List<Pais> paises = new ArrayList<Pais>();
		try {
			paises = (List<Pais>) paisService.findAllPais();
			return new ResponseEntity<List<Pais>>(paises, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Pais> get(@PathVariable("id") Long id) {
		try {
			Pais pais = paisService.findById(id);
			return new ResponseEntity<Pais>(pais, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	
}
