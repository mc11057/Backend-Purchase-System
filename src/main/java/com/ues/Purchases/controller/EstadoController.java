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

import com.ues.Purchases.model.Estado;
import com.ues.Purchases.service.IEstadoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/estado")
public class EstadoController {
	
	@Autowired
	private IEstadoService estadoService;
	
	
	public EstadoController(IEstadoService estadoService ) {
		this.estadoService = estadoService;

	}
	
	
	@GetMapping()
	public ResponseEntity<List<Estado>> getEstado() {

		List<Estado> estados = new ArrayList<Estado>();
		try {
			estados = (List<Estado>) estadoService.findAllEstado();
			return new ResponseEntity<List<Estado>>(estados, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Estado> get(@PathVariable("id") Long id) {
		try {
			Estado estado = estadoService.findById(id);
			return new ResponseEntity<Estado>(estado, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	

}
