package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.TipoProducto;

public interface ITipoProductoService {

	List<TipoProducto> findAll() throws Exception ;

	TipoProducto findById(Long id) throws Exception;
	
	
    List<TipoProducto> obtenerActivos() throws Exception;
	
	
}
