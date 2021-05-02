package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.PedidoProducto;

public interface IPedidoProductoService {

	List<PedidoProducto> findByPedido(Long pedidoId) throws Exception;;
}
