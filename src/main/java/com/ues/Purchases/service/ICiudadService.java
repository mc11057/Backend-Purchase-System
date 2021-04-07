package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Ciudad;

public interface ICiudadService {
	
	 List<Ciudad> findAllCiudad() throws Exception ;

	 Ciudad findById(Long id) throws Exception;

}
