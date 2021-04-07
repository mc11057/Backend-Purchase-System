package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Ciudad;
import com.ues.Purchases.repository.ICiudadRepository;
import com.ues.Purchases.service.ICiudadService;

@Service
public class CiudadServiceImpl implements ICiudadService {

	
	@Autowired
	private ICiudadRepository ciudadRepository;
	
	@Override
	public List<Ciudad> findAllCiudad() throws Exception {
		// TODO Auto-generated method stub
		return (List<Ciudad>)ciudadRepository.findAll();
	}

	@Override
	public Ciudad findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return ciudadRepository.findById(id).orElse(null);
	}

}
