package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.ues.Purchases.model.Pago;

public interface IPagoRepository extends JpaRepository<Pago, Long>{
	
	 @Procedure
	    void crear_pago( long id_factura_orden_pago , long id_moneda ,long id_forma_pago ,String usuario);
	 
	 @Query(value = "select * from pago where estado='A'" , nativeQuery = true)
		 List<Pago> obtenerPagoActivos();

}
