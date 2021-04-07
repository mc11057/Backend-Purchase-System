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

import com.ues.Purchases.model.Departamento;
import com.ues.Purchases.service.IDepartamentoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/departamento")
public class DepartamentoController {
	
	
	@Autowired
	private IDepartamentoService departamentoService;

	public DepartamentoController(IDepartamentoService departamentoService) {
		super();
		this.departamentoService = departamentoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Departamento>> getDepartamento() {

		List<Departamento> departamentos = new ArrayList<Departamento>();
		try {
			departamentos = (List<Departamento>) departamentoService.findAllDepartamento();
			return new ResponseEntity<List<Departamento>>(departamentos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Departamento> get(@PathVariable("id") Long id) {
		try {
			Departamento departamento = departamentoService.findById(id);
			return new ResponseEntity<Departamento>(departamento, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
