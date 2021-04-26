package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Puesto;

public interface IPuestoService {
	
	 List<Puesto> findAll() throws Exception ;

	 Puesto findById(Long id) throws Exception;

}
