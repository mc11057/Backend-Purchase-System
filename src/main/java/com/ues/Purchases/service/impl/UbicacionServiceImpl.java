package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Ubicacion;
import com.ues.Purchases.repository.IUbicacionRepository;
import com.ues.Purchases.service.IUbicacionService;

@Service
public class UbicacionServiceImpl implements IUbicacionService {
	
	@Autowired
	private IUbicacionRepository ubicacionRepository;

	@Override
	public List<Ubicacion> findAllUbicacion() throws Exception {
		// TODO Auto-generated method stub
		return (List<Ubicacion>) ubicacionRepository.findAll();
	}

	@Override
	public Ubicacion findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return ubicacionRepository.findById(id).orElse(null);
	}
	
	

}
