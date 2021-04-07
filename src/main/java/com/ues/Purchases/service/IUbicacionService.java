package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Ubicacion;

public interface IUbicacionService {
	
	List<Ubicacion> findAllUbicacion() throws Exception ;

	Ubicacion findById(Long id) throws Exception;

}
