package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Pedido;
import com.ues.Purchases.repository.IPedidoRepository;
import com.ues.Purchases.service.IPedidoService;

@Service
public class PedidoServiceImpl  implements IPedidoService {
	
	@Autowired
	private IPedidoRepository pedidoRepository;

	public PedidoServiceImpl(IPedidoRepository pedidoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
	}

	
	@Override
	public List<Pedido> findAll() throws Exception {
		return (List<Pedido>)pedidoRepository.findAll();
	}

	@Override
	public Pedido findById(Long id) throws Exception {
		return pedidoRepository.findById(id).orElse(null);
	}

}
