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

import com.ues.Purchases.model.Horario;
import com.ues.Purchases.service.IHorarioService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/horario")
public class HorarioController {
	
	@Autowired
	private IHorarioService horarioService;

	public HorarioController(IHorarioService horarioService) {
		super();
		this.horarioService = horarioService;
	}
	
	
	@GetMapping()
	public ResponseEntity<List<Horario>> getHorario() {

		List<Horario> horarios = new ArrayList<Horario>();
		try {
			horarios = (List<Horario>) horarioService.findAllHorario();
			return new ResponseEntity<List<Horario>>(horarios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Horario> get(@PathVariable("id") Long id) {
		try {
			Horario horarios = horarioService.findById(id);
			return new ResponseEntity<Horario>(horarios, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
