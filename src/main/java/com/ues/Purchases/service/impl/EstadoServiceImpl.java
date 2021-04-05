package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Estado;
import com.ues.Purchases.repository.IEstadoRepository;
import com.ues.Purchases.service.IEstadoService;

@Service
public class EstadoServiceImpl implements IEstadoService {
	
	@Autowired
	private IEstadoRepository estadorepository;

	@Override
	public List<Estado> findAllEstado() throws Exception {
		// TODO Auto-generated method stub
		return (List<Estado>) estadorepository.findAll();
	}

	@Override
	public Estado findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return estadorepository.findById(id).orElse(null);
	}

}
