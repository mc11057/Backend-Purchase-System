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

import com.ues.Purchases.model.ProgresoPedido;
import com.ues.Purchases.service.IProgresoPedidoService;
import com.ues.Purchases.service.impl.ProgresoPedidoImpl;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/progresopedido")
public class ProgresoPedidoController {

	@Autowired
	private IProgresoPedidoService prgresoPedidoService;
	
	public ProgresoPedidoController(ProgresoPedidoImpl prgresoPedidoService) {
		super();
		this.prgresoPedidoService = prgresoPedidoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<ProgresoPedido>> getBarrio() {

		List<ProgresoPedido> progresoPedidos = new ArrayList<ProgresoPedido>();
		try {
			progresoPedidos = (List<ProgresoPedido>) prgresoPedidoService.obtenerActivos();
			return new ResponseEntity<List<ProgresoPedido>>(progresoPedidos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProgresoPedido> get(@PathVariable("id") Long id) {
		try {
			ProgresoPedido progresoPedido = prgresoPedidoService.findById(id);
			return new ResponseEntity<ProgresoPedido>(progresoPedido, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
}
