package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.ProgresoPedido;

public interface IProgresoPedidoService {
	
	List<ProgresoPedido> findAll() throws Exception ;

	ProgresoPedido findById(Long id) throws Exception;
	
	
    List<ProgresoPedido> obtenerActivos() throws Exception;

}
