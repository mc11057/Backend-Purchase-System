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

import com.ues.Purchases.model.TipoProducto;
import com.ues.Purchases.service.ITipoProductoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/tipoproducto")
public class TipoProductoController {
	
	@Autowired
	private ITipoProductoService tipoProductoService;

	public TipoProductoController(ITipoProductoService tipoProductoService) {
		super();
		this.tipoProductoService = tipoProductoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<TipoProducto>> getCategoriaProducto() {

		List<TipoProducto> tipoProdcutos = new ArrayList<TipoProducto>();
		try {
			tipoProdcutos = (List<TipoProducto>) tipoProductoService.obtenerActivos();
			return new ResponseEntity<List<TipoProducto>>(tipoProdcutos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TipoProducto> get(@PathVariable("id") Long id) {
		try {
			TipoProducto tipoProducto = tipoProductoService.findById(id);
			return new ResponseEntity<TipoProducto>(tipoProducto, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
