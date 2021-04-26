package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Documento;

public interface IDocumentoService {
	
	 List<Documento> findAll() throws Exception ;

	 Documento findById(Long id) throws Exception;

}
