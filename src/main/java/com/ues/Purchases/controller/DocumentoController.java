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

import com.ues.Purchases.model.Documento;
import com.ues.Purchases.service.IDocumentoService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("app/v1/documento")
public class DocumentoController {
	
	
	
	@Autowired
	private IDocumentoService documentoService;

	public DocumentoController(IDocumentoService documentoService) {
		super();
		this.documentoService = documentoService;
	}
	
	@GetMapping()
	public ResponseEntity<List<Documento>> getAll() {

		List<Documento> documentos = new ArrayList<Documento>();
		try {
			documentos = (List<Documento>) documentoService.findAll();
			return new ResponseEntity<List<Documento>>(documentos, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Documento> get(@PathVariable("id") Long id) {
		try {
			Documento documento = documentoService.findById(id);
			return new ResponseEntity<Documento>(documento, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
