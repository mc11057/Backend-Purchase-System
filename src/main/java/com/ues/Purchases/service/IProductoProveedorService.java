package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.ProductoProveedor;

public interface IProductoProveedorService {
	
	
	 List<ProductoProveedor> findAll() throws Exception ;
	 ProductoProveedor create(ProductoProveedor productoProveedor) throws Exception;

}
