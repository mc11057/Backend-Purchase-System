package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.ProgresoPedido;
import com.ues.Purchases.repository.IProgresoPedidoRepository;
import com.ues.Purchases.service.IProgresoPedidoService;

@Service
public class ProgresoPedidoImpl implements IProgresoPedidoService{
	
	@Autowired
	private IProgresoPedidoRepository progresoPedidoRepository;

	@Override
	public List<ProgresoPedido> findAll() throws Exception {
		// TODO Auto-generated method stub
		return progresoPedidoRepository.findAll();
	}

	@Override
	public ProgresoPedido findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return progresoPedidoRepository.findById(id).orElse(null);
	}

	@Override
	public List<ProgresoPedido> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return progresoPedidoRepository.obtenerActivos();
	}
	
	
	

}
