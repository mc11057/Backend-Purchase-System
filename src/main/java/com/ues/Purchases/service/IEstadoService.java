package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Estado;


public interface IEstadoService {
	
	 List<Estado> findAllEstado() throws Exception ;

	 Estado findById(Long id) throws Exception;


}
