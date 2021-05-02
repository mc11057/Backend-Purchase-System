package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.Pedido;
import com.ues.Purchases.model.PedidoProducto;

public interface IPedidoProductoRepository extends JpaRepository<PedidoProducto, Long>{
	
	List<PedidoProducto> findByPedido(Pedido pedido);

}
