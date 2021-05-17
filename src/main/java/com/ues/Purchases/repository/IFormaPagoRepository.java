package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.FormaPago;

public interface IFormaPagoRepository  extends JpaRepository<FormaPago, Long> {
	
	@Query(value = "select * from forma_pago where Estado='A'" , nativeQuery = true)
	 List<FormaPago> obtenerFormaPagoActivos();
	
	

}
