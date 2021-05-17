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

import com.ues.Purchases.model.FacturaOrdenPago;
import com.ues.Purchases.service.IFacturaOrdenPagoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/facturaordenpago")
public class FacturaOrdenPagoController {
	
	@Autowired
	private IFacturaOrdenPagoService facturaordenpagoService;

	public FacturaOrdenPagoController(IFacturaOrdenPagoService facturaordenpagoService) {
		super();
		this.facturaordenpagoService = facturaordenpagoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<FacturaOrdenPago>> getFacturaOrdenPago() {

		List<FacturaOrdenPago> facturasordenespago = new ArrayList<FacturaOrdenPago>();
		try {
			facturasordenespago = (List<FacturaOrdenPago>) facturaordenpagoService.obtenerFacturaOrdenPagoActivos();
			return new ResponseEntity<List<FacturaOrdenPago>>(facturasordenespago, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FacturaOrdenPago> get(@PathVariable("id") Long id) {
		try {
			FacturaOrdenPago factura = facturaordenpagoService.findById(id);
			return new ResponseEntity<FacturaOrdenPago>(factura, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


}
