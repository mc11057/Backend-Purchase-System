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

import com.ues.Purchases.model.Nacionalidad;
import com.ues.Purchases.service.INacionalidadService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/nacionalidad")
public class NacionalidadController {
	
	@Autowired
	private INacionalidadService nacionalidadService;

	public NacionalidadController(INacionalidadService nacionalidadService) {
		super();
		this.nacionalidadService = nacionalidadService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Nacionalidad>> getNacionalidad() {

		List<Nacionalidad> nacionalidades = new ArrayList<Nacionalidad>();
		try {
			nacionalidades = (List<Nacionalidad>) nacionalidadService.obtenerActivos();
			return new ResponseEntity<List<Nacionalidad>>(nacionalidades, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Nacionalidad> get(@PathVariable("id") Long id) {
		try {
			Nacionalidad nacionalidad = nacionalidadService.findById(id);
			return new ResponseEntity<Nacionalidad>(nacionalidad, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	

}
