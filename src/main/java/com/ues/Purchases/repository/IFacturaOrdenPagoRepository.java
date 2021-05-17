package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.FacturaOrdenPago;

public interface IFacturaOrdenPagoRepository extends JpaRepository<FacturaOrdenPago, Long> {
	
	@Query(value = "select * from factura_orden_pago where Estado='A'" , nativeQuery = true)
	 List<FacturaOrdenPago> obtenerFacturaOrdenPagoActivos();

}
