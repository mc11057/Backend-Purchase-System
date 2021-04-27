package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Nacionalidad;
import com.ues.Purchases.repository.INacionalidadRepository;
import com.ues.Purchases.service.INacionalidadService;

@Service
public class NacionalidadServiceImpl implements INacionalidadService{
	
	@Autowired
	private INacionalidadRepository nacionalidadRepository;
	
	

	public NacionalidadServiceImpl(INacionalidadRepository nacionalidadRepository) {
		super();
		this.nacionalidadRepository = nacionalidadRepository;
	}

	@Override
	public List<Nacionalidad> findAll() throws Exception {
		// TODO Auto-generated method stub
		return nacionalidadRepository.findAll();
	}

	@Override
	public Nacionalidad findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return nacionalidadRepository.findById(id).orElse(null);
	}

	@Override
	public List<Nacionalidad> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return nacionalidadRepository.obtenerActivos();
	}

}
