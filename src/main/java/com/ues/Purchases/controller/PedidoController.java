package com.ues.Purchases.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ues.Purchases.model.Pedido;
import com.ues.Purchases.service.IPedidoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/pedido")
public class PedidoController {
	
	
	
	@Autowired
	private IPedidoService pedidoService;

	public PedidoController(IPedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Pedido>> getAll() {

		List<Pedido> pedidos = new ArrayList<Pedido>();
		try {
			pedidos = (List<Pedido>) pedidoService.obtenerPedidosActivos();
			return new ResponseEntity<List<Pedido>>(pedidos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> get(@PathVariable("id") Long id) {
		try {
			Pedido empleado = pedidoService.findById(id);
			return new ResponseEntity<Pedido>(empleado, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	@PostMapping()
	public ResponseEntity<Object> create(@RequestBody Pedido pedido) {
		try {
			Pedido pedidoDB = pedidoService.create(pedido);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(pedidoDB.getPedidoId()).toUri();
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/aprobar")
	public ResponseEntity<Object> aprobarpedido(@RequestBody Pedido pedido) {
		
	try {
			pedidoService.aprobarPedido(pedido);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	
	@PutMapping("/denegar")
	public ResponseEntity<Object> denegarpedido(@RequestBody Pedido pedido) {
		
		try {
			pedidoService.denegarPedido(pedido);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
}
