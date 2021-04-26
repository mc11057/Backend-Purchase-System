package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Nacionalidad;

public interface INacionalidadService {
	
	
	 List<Nacionalidad> findAll() throws Exception ;

	 Nacionalidad findById(Long id) throws Exception;

}
