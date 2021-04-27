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

import com.ues.Purchases.model.CategoriaProducto;
import com.ues.Purchases.service.ICategoriaProductoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/categoriaproducto")
public class CategoriaProductoController {
	
	@Autowired
	private ICategoriaProductoService categoriaProductoService;

	public CategoriaProductoController(ICategoriaProductoService categoriaProductoService) {
		super();
		this.categoriaProductoService = categoriaProductoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<CategoriaProducto>> getCategoriaProducto() {

		List<CategoriaProducto> categoriaProdcutos = new ArrayList<CategoriaProducto>();
		try {
			categoriaProdcutos = (List<CategoriaProducto>) categoriaProductoService.obtenerActivos();
			return new ResponseEntity<List<CategoriaProducto>>(categoriaProdcutos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaProducto> get(@PathVariable("id") Long id) {
		try {
			CategoriaProducto categoriaProducto = categoriaProductoService.findById(id);
			return new ResponseEntity<CategoriaProducto>(categoriaProducto, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
