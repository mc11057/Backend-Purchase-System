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

import com.ues.Purchases.model.Barrio;
import com.ues.Purchases.service.IBarrioService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/barrio")
public class BarrioController {
	
	@Autowired
	private IBarrioService barrioService;

	public BarrioController(IBarrioService barrioService) {
		super();
		this.barrioService = barrioService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Barrio>> getBarrio() {

		List<Barrio> barrios = new ArrayList<Barrio>();
		try {
			barrios = (List<Barrio>) barrioService.findAllBarrio();
			return new ResponseEntity<List<Barrio>>(barrios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Barrio> get(@PathVariable("id") Long id) {
		try {
			Barrio barrios = barrioService.findById(id);
			return new ResponseEntity<Barrio>(barrios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
