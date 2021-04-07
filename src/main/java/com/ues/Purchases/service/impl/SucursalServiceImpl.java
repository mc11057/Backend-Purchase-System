package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Sucursal;
import com.ues.Purchases.repository.ISucursalRepository;
import com.ues.Purchases.service.ISucursalService;

@Service
public class SucursalServiceImpl implements ISucursalService {
	
	@Autowired
	private ISucursalRepository sucursalRepository;

	@Override
	public List<Sucursal> findAllSucursal() throws Exception {
		
		return sucursalRepository.findAll();
	}

	@Override
	public Sucursal findById(Long id) throws Exception {
		
		return sucursalRepository.findById(id).orElse(null);
	}

}
