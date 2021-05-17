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

import com.ues.Purchases.model.FormaPago;
import com.ues.Purchases.service.IFormaPagoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/formapago")
public class FormaPagoController {
	
	
	@Autowired
	private IFormaPagoService formapagoService;

	public FormaPagoController(IFormaPagoService formapagoService) {
		super();
		this.formapagoService = formapagoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<FormaPago>> getFormaPago() {

		List<FormaPago> formaspagos = new ArrayList<FormaPago>();
		try {
			formaspagos = (List<FormaPago>) formapagoService.obtenerFormaPagoActivos();
			return new ResponseEntity<List<FormaPago>>(formaspagos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPago> get(@PathVariable("id") Long id) {
		try {
			FormaPago formapago = formapagoService.findById(id);
			return new ResponseEntity<FormaPago>(formapago, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
