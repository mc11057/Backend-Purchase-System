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

import com.ues.Purchases.model.FormaPago;
import com.ues.Purchases.model.Moneda;
import com.ues.Purchases.service.IMonedaService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/moneda")
public class MonedaController {
	
	
	@Autowired
	private IMonedaService monedaService;

	public MonedaController(IMonedaService monedaService) {
		super();
		this.monedaService = monedaService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Moneda>> getMoneda() {

		List<Moneda> monedas = new ArrayList<Moneda>();
		try {
			monedas = (List<Moneda>) monedaService.obtenerMonedaActivos();
			return new ResponseEntity<List<Moneda>>(monedas, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Moneda> get(@PathVariable("id") Long id) {
		try {
			Moneda moneda = monedaService.findById(id);
			return new ResponseEntity<Moneda>(moneda, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
