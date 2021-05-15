package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.ProductoProveedor;
import com.ues.Purchases.repository.IProductoProveedorRepository;
import com.ues.Purchases.service.IProductoProveedorService;

@Service
public class ProductoProveedorServiceImpl implements IProductoProveedorService{
	
	@Autowired
	private IProductoProveedorRepository productoProveedorRepository;
	
	public ProductoProveedorServiceImpl(IProductoProveedorRepository productoProveedorRepository) {
		super();
		this.productoProveedorRepository = productoProveedorRepository;
	}

	
	@Override
	public List<ProductoProveedor> findAll() throws Exception {
		return productoProveedorRepository.findAll();
	}

	@Override
	public ProductoProveedor create(ProductoProveedor productoProveedor) throws Exception {
	
		return productoProveedorRepository.save(productoProveedor);
	}

}
