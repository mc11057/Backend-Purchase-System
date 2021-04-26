package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.TipoIdentificacion;

public interface ITipoIdentificacionService {
	
	
	 List<TipoIdentificacion> findAll() throws Exception ;

	 TipoIdentificacion findById(Long id) throws Exception;

}
