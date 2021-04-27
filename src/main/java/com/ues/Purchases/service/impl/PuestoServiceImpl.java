package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Puesto;
import com.ues.Purchases.repository.IPuestoRepository;
import com.ues.Purchases.service.IPuestoService;

@Service
public class PuestoServiceImpl implements IPuestoService{
	
	@Autowired
	private IPuestoRepository puestoRepository;

	@Override
	public List<Puesto> findAll() throws Exception {
		// TODO Auto-generated method stub
		return puestoRepository.findAll();
	}

	@Override
	public Puesto findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return puestoRepository.findById(id).orElse(null);
	}

	@Override
	public List<Puesto> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return puestoRepository.obtenerActivos();
	}
	
	

}
