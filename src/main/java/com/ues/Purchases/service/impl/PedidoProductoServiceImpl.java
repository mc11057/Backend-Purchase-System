package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.PedidoProducto;
import com.ues.Purchases.repository.IPedidoProductoRepository;
import com.ues.Purchases.repository.IPedidoRepository;
import com.ues.Purchases.service.IPedidoProductoService;

@Service
public class PedidoProductoServiceImpl implements IPedidoProductoService{

	
	@Autowired
	private IPedidoProductoRepository pedidoProductoRepository;
	@Autowired
	private IPedidoRepository pedidoRepository;
	
	@Override
	public List<PedidoProducto> findByPedido(Long pedidoId) {
		
		return pedidoProductoRepository.findByPedido(pedidoRepository.findById(pedidoId).get());

	}

}
