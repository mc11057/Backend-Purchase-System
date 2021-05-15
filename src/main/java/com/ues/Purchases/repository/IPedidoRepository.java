package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.ues.Purchases.model.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido,Long>{
	

	@Procedure
    void APROBAR_PEDIDO(long Id_Pedido,String usuario);

    @Procedure
    void denegar_pedido(long ID_Pedido,String usuario);
	
	
	@Query(value = "select p.* from pedido p inner join progreso_pedido pp on p.pro_pedi_id=pp.pro_pedi_id\r\n"
			+ "where p.Estado='A' and estado_pedido='Activo'" , nativeQuery = true)
	 List<Pedido> obtenerPedidosActivos();
	
	

}
