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

import com.ues.Purchases.model.Puesto;
import com.ues.Purchases.service.IPuestoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/puesto")
public class PuestoController {
	
	@Autowired
	private IPuestoService puestoService;
	
	public PuestoController(IPuestoService puestoService) {
		this.puestoService=puestoService;
	}
	
	
	@GetMapping()
	public ResponseEntity<List<Puesto>> getAll() {

		List<Puesto> puestos = new ArrayList<Puesto>();
		try {
			puestos = (List<Puesto>) puestoService.obtenerActivos();
			return new ResponseEntity<List<Puesto>>(puestos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Puesto> get(@PathVariable("id") Long id) {
		try {
			Puesto puesto = puestoService.findById(id);
			return new ResponseEntity<Puesto>(puesto, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
