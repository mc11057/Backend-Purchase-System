package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Pedido;

public interface IPedidoService {
	
	
	 List<Pedido> findAll() throws Exception ;

	 Pedido findById(Long id) throws Exception;
	 
	 Pedido create(Pedido pedido) throws Exception;
	 
	  void aprobarPedido(long pedidoId,String usuario) throws Exception;
	 
	 void denegarPedido(long pedidoId,String usuario) throws Exception;
	 
	 List<Pedido> obtenerPedidosActivos() throws Exception ;

}
