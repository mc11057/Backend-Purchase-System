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

import com.ues.Purchases.model.Ubicacion;
import com.ues.Purchases.service.IUbicacionService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/ubicacion")
public class UbicacionController {
	
	@Autowired
	private IUbicacionService ubicacionService;
	
	public UbicacionController(IUbicacionService ubicacionService) {
		this.ubicacionService=ubicacionService;
	}
	
	
	@GetMapping()
	public ResponseEntity<List<Ubicacion>> getUbicacion() {

		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		try {
			ubicaciones = (List<Ubicacion>) ubicacionService.findAllUbicacion();
			return new ResponseEntity<List<Ubicacion>>(ubicaciones, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Ubicacion> get(@PathVariable("id") Long id) {
		try {
			Ubicacion ubicaciones = ubicacionService.findById(id);
			return new ResponseEntity<Ubicacion>(ubicaciones, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


}
