package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Proveedor;
import com.ues.Purchases.repository.IProveedorRepository;
import com.ues.Purchases.service.IProveedorService;
@Service
public class ProveedorServiceImpl implements IProveedorService{
	
	@Autowired
	private IProveedorRepository repository;
	

	public ProveedorServiceImpl(IProveedorRepository repository) {
		super();
		this.repository = repository;
	}


	@Override
	public List<Proveedor> findAll() throws Exception {
		return (List<Proveedor>) repository.findAll();
	}

}
