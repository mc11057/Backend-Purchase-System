package com.ues.Purchases.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.Purchases.model.Proveedor;
import com.ues.Purchases.service.IProveedorService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/proveedor")
public class ProveedorController {
	
	
	
	@Autowired
	private IProveedorService  service;

	public ProveedorController(IProveedorService service) {
		super();
		this.service = service;
	}
	
	@GetMapping()
	public ResponseEntity<List<Proveedor>> getAll() {

		List<Proveedor> proveedores = new ArrayList<Proveedor>();
		try {
			proveedores = (List<Proveedor>) service.findAll();
			return new ResponseEntity<List<Proveedor>>(proveedores, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	

}
