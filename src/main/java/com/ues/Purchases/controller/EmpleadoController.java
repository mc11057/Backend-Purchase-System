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

import com.ues.Purchases.model.Empleado;
import com.ues.Purchases.service.IEmpleadoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/empleado")
public class EmpleadoController {
	
	
	@Autowired
	private IEmpleadoService empleadoService;

	public EmpleadoController(IEmpleadoService empleadoService) {
		super();
		this.empleadoService = empleadoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Empleado>> getAll() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		try {
			empleados = (List<Empleado>) empleadoService.findAll();
			return new ResponseEntity<List<Empleado>>(empleados, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Empleado> get(@PathVariable("id") Long id) {
		try {
			Empleado empleado = empleadoService.findById(id);
			return new ResponseEntity<Empleado>(empleado, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	@GetMapping("/porUsuario/{userId}")
	public ResponseEntity<Empleado> getEmpleadoByUserId(@PathVariable("userId") Long userId) {

		Empleado empleado = new Empleado();
		try {
			empleado = empleadoService.findByApplicationUser(userId);
			return new ResponseEntity<Empleado>(empleado, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
