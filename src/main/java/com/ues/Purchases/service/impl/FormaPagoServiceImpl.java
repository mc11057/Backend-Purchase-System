package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.FormaPago;
import com.ues.Purchases.repository.IFormaPagoRepository;
import com.ues.Purchases.service.IFormaPagoService;

@Service
public class FormaPagoServiceImpl implements IFormaPagoService{
	
	@Autowired
	private IFormaPagoRepository formaPagoRepository;
	

	@Override
	public FormaPago findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return formaPagoRepository.findById(id).orElse(null);
	}


	@Override
	public List<FormaPago> obtenerFormaPagoActivos() throws Exception {
		// TODO Auto-generated method stub
		return (List<FormaPago>)formaPagoRepository.obtenerFormaPagoActivos();
	}



}
