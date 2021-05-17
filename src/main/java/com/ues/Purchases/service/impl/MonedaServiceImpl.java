package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Moneda;
import com.ues.Purchases.repository.IMonedaRepository;
import com.ues.Purchases.service.IMonedaService;

@Service
public class MonedaServiceImpl implements IMonedaService{
	
	@Autowired
	private IMonedaRepository monedaRepository;
	

	@Override
	public Moneda findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return monedaRepository.findById(id).orElse(null);
	}

	@Override
	public List<Moneda> obtenerMonedaActivos() throws Exception {
		// TODO Auto-generated method stub
		return (List<Moneda>)monedaRepository.obtenerMonedaActivos();
	}
	

}
