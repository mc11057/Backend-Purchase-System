package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Pais;
import com.ues.Purchases.repository.IPaisRepository;
import com.ues.Purchases.service.IPaisService;

@Service
public class PaisServiceImpl implements IPaisService {

	@Autowired
	private IPaisRepository paisRepository;

	@Override
	public List<Pais> findAllPais() throws Exception {
		return (List<Pais>) paisRepository.findAll();
	}

	@Override
	public Pais findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return paisRepository.findById(id).orElse(null);

	}

}
