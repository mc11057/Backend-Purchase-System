package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.Purchases.model.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido,Long>{
	

	@Query(value = "{call aprobar_pedido(:pedidoId,:usuario)}", nativeQuery = true)
    void aprobarPedido(@Param("pedidoId") long pedidoId,@Param("usuario") String usuario);
	
	@Query(value = "{call denegar_pedido(:pedidoId,:usuario)}", nativeQuery = true)
    void denegarPedido(@Param("pedidoId") long pedidoId,@Param("usuario") String usuario);
	
	@Query(value = "select p.* from pedido p inner join progreso_pedido pp on p.pro_pedi_id=pp.pro_pedi_id\r\n"
			+ "where p.Estado='A' and estado_pedido='Activo'" , nativeQuery = true)
	 List<Pedido> obtenerPedidosActivos();
	
	

}
