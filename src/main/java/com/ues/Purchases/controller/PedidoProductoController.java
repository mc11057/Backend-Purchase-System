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

import com.ues.Purchases.model.PedidoProducto;
import com.ues.Purchases.service.IPedidoProductoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/pedido-detail")
public class PedidoProductoController {
	
	@Autowired
	private IPedidoProductoService service;

	public PedidoProductoController(IPedidoProductoService service) {
		super();
		this.service = service;
	}
	@GetMapping("/{pedidoId}")
	public ResponseEntity<List<PedidoProducto>> getProductsByPedido(@PathVariable("pedidoId") Long pedidoId) {
		
		List<PedidoProducto> productos = new ArrayList<PedidoProducto>();
		try {
			productos = (List<PedidoProducto>) service.findByPedido(pedidoId);
			return new ResponseEntity<List<PedidoProducto>>(productos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	


}
