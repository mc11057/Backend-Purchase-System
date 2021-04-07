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

import com.ues.Purchases.model.Sucursal;
import com.ues.Purchases.service.ISucursalService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/sucursal")
public class SucursalController {
	
	@Autowired
	private ISucursalService sucursalService;

	public SucursalController(ISucursalService sucursalService) {
		super();
		this.sucursalService = sucursalService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Sucursal>> getSucursal() {
		
		List<Sucursal> sucursales = new ArrayList<Sucursal>();
		
		try {
			sucursales = sucursalService.findAllSucursal();
			return new ResponseEntity<List<Sucursal>>(sucursales, HttpStatus.OK);
		} catch (NotFoundException ne) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Sucursal> get(@PathVariable("id") Long id) {
		try {
			Sucursal sucursales = sucursalService.findById(id);
			return new ResponseEntity<Sucursal>(sucursales, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
