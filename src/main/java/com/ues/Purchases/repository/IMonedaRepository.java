package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.Moneda;

public interface IMonedaRepository  extends JpaRepository<Moneda, Long>  {
	
	@Query(value = "select * from moneda where Estado='A'" , nativeQuery = true)
	 List<Moneda> obtenerMonedaActivos();

}
