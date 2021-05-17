package com.ues.Purchases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.Purchases.model.Pago;
import com.ues.Purchases.service.IPagoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/pago")
public class PagoController {
	
	
	@Autowired
	private IPagoService pagoService;

	public PagoController(IPagoService pagoService) {
		super();
		this.pagoService = pagoService;
	}
	
	@PostMapping()
	public ResponseEntity<Object> crearPago(@RequestBody Pago pago, String usuario) {
		
	try {
		pagoService.crearPago(pago, usuario);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

}
