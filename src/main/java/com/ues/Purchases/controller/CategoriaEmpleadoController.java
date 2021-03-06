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

import com.ues.Purchases.model.CategoriaEmpleado;
import com.ues.Purchases.service.ICategoriaEmpleadoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/categoriaempleado")
public class CategoriaEmpleadoController {
	
	@Autowired
	private ICategoriaEmpleadoService categoriaEmpleadoService;

	public CategoriaEmpleadoController(ICategoriaEmpleadoService categoriaEmpleadoService) {
		super();
		this.categoriaEmpleadoService = categoriaEmpleadoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<CategoriaEmpleado>> getAll() {

		List<CategoriaEmpleado> categoriaEmpleados = new ArrayList<CategoriaEmpleado>();
		try {
			categoriaEmpleados = (List<CategoriaEmpleado>) categoriaEmpleadoService.obtenerActivos();
			return new ResponseEntity<List<CategoriaEmpleado>>(categoriaEmpleados, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaEmpleado> get(@PathVariable("id") Long id) {
		try {
			CategoriaEmpleado categoriaEmpleado = categoriaEmpleadoService.findById(id);
			return new ResponseEntity<CategoriaEmpleado>(categoriaEmpleado, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
