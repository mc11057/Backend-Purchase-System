package com.ues.Purchases.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ues.Purchases.model.ProductoProveedor;
import com.ues.Purchases.service.IProductoProveedorService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/producto-proveedor")
public class ProductoProveedorController {
	
	
	
	@Autowired
	private IProductoProveedorService service;

	public ProductoProveedorController(IProductoProveedorService service) {
		super();
		this.service = service;
	}
	
	@GetMapping()
	public ResponseEntity<List<ProductoProveedor>> getAll() {

		List<ProductoProveedor> productosProveedor = new ArrayList<ProductoProveedor>();
		try {
			productosProveedor = (List<ProductoProveedor>) service.findAll();
			return new ResponseEntity<List<ProductoProveedor>>(productosProveedor, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping()
	public ResponseEntity<Object> create(@RequestBody ProductoProveedor productoProveedor) {
		try {
			ProductoProveedor productoProveedorDB = service.create(productoProveedor);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(productoProveedorDB.getId()).toUri();
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}
