package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Departamento;
import com.ues.Purchases.repository.IDepartamentoRepository;
import com.ues.Purchases.service.IDepartamentoService;

@Service
public class DepartamentoServiceImpl implements IDepartamentoService {
	
	@Autowired
	private IDepartamentoRepository departamentoRepository;

	@Override
	public List<Departamento> findAllDepartamento() throws Exception {
		// TODO Auto-generated method stub
		return (List<Departamento>)departamentoRepository.findAll();
	}

	@Override
	public Departamento findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return departamentoRepository.findById(id).orElse(null);
	}

	
}
