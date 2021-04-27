package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.CategoriaProducto;

public interface ICategoriaProductoService {
	
	List<CategoriaProducto> findAll() throws Exception ;

	CategoriaProducto findById(Long id) throws Exception;
	
	
    List<CategoriaProducto> obtenerActivos() throws Exception;

}
