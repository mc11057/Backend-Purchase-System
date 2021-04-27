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

import com.ues.Purchases.model.TipoIdentificacion;
import com.ues.Purchases.service.ITipoIdentificacionService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/tipoidentificacion")
public class TipoIdentificacionController {
	
	@Autowired
	private ITipoIdentificacionService tipoIdentificacionService;

	public TipoIdentificacionController(ITipoIdentificacionService tipoIdentificacionService) {
		super();
		this.tipoIdentificacionService = tipoIdentificacionService;
	}
	
	@GetMapping()
	public ResponseEntity<List<TipoIdentificacion>> getCategoriaProducto() {

		List<TipoIdentificacion> tipoIdentificaciones = new ArrayList<TipoIdentificacion>();
		try {
			tipoIdentificaciones = (List<TipoIdentificacion>) tipoIdentificacionService.obtenerActivos();
			return new ResponseEntity<List<TipoIdentificacion>>(tipoIdentificaciones, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TipoIdentificacion> get(@PathVariable("id") Long id) {
		try {
			TipoIdentificacion tipoIdentificacion = tipoIdentificacionService.findById(id);
			return new ResponseEntity<TipoIdentificacion>(tipoIdentificacion, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
