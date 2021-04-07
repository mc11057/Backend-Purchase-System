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

import com.ues.Purchases.model.TipoHorario;
import com.ues.Purchases.service.ITipoHorarioService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/tipohorario")
public class TipoHorarioController {
	
	@Autowired
	private ITipoHorarioService tipoHorarioService;
	
	public TipoHorarioController(ITipoHorarioService tipoHorarioService) {
		this.tipoHorarioService=tipoHorarioService;
	}
	
	
	@GetMapping()
	public ResponseEntity<List<TipoHorario>> getTipoHorario() {

		List<TipoHorario> tipoHorarios = new ArrayList<TipoHorario>();
		try {
			tipoHorarios = (List<TipoHorario>) tipoHorarioService.findAllTipoHorario();
			return new ResponseEntity<List<TipoHorario>>(tipoHorarios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TipoHorario> get(@PathVariable("id") Long id) {
		try {
			TipoHorario tipoHorarios = tipoHorarioService.findById(id);
			return new ResponseEntity<TipoHorario>(tipoHorarios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
