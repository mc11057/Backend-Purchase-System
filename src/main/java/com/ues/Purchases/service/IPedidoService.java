package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Pedido;

public interface IPedidoService {
	
	
	 List<Pedido> findAll() throws Exception ;

	 Pedido findById(Long id) throws Exception;
	 
	 Pedido create(Pedido pedido) throws Exception;
	 
	  void aprobarPedido(Pedido pedido) throws Exception;
	 
	 void denegarPedido(Pedido pedido) throws Exception;
	 
	 List<Pedido> obtenerPedidosActivos() throws Exception ;

}
