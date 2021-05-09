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

import com.ues.Purchases.model.Producto;
import com.ues.Purchases.service.IProductoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/producto")
public class ProductoController {
	

	@Autowired
	private IProductoService productoService;

	public ProductoController(IProductoService productoService) {
		super();
		this.productoService = productoService;
	}
	
	
	
	@GetMapping()
	public ResponseEntity<List<Producto>> getAll() {

		List<Producto> productos = new ArrayList<Producto>();
		try {
			productos = (List<Producto>) productoService.findAll();
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/porcategoria/{categoriaId}")
	public ResponseEntity<List<Producto>> getByCategory(@PathVariable("categoriaId") Long categoriaId) {

		List<Producto> productos = new ArrayList<Producto>();
		try {
			productos = (List<Producto>) productoService.findByCategoriaProducto(categoriaId);
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
