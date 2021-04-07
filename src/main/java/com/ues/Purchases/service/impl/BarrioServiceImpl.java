package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Barrio;
import com.ues.Purchases.repository.IBarrioRepository;
import com.ues.Purchases.service.IBarrioService;

@Service
public class BarrioServiceImpl implements IBarrioService {
	
	@Autowired
	private IBarrioRepository barrioRepository;

	@Override
	public List<Barrio> findAllBarrio() throws Exception {
		// TODO Auto-generated method stub
		return (List<Barrio>)barrioRepository.findAll();
	}

	@Override
	public Barrio findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return barrioRepository.findById(id).orElse(null);
	}

}
