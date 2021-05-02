package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Pedido;

public interface IPedidoService {
	
	
	 List<Pedido> findAll() throws Exception ;

	 Pedido findById(Long id) throws Exception;

}
