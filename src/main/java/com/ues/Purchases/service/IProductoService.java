package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Producto;

public interface IProductoService {
	
	
	 List<Producto> findAll() throws Exception ;

	 Producto findById(Long id) throws Exception;
	
	 List<Producto> findByCategoriaProducto(Long categoriaProductoId) throws Exception;;


}
