package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.ProgresoPedido;


public interface IProgresoPedidoRepository extends JpaRepository<ProgresoPedido, Long>{
	
	@Query(value = "SELECT * FROM progreso_pedido WHERE estado='A'", nativeQuery=true)
    List<ProgresoPedido> obtenerActivos();

}
