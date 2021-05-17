package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Moneda;

public interface IMonedaService {
	
	Moneda findById(Long id) throws Exception;


	List<Moneda> obtenerMonedaActivos() throws Exception;

}
