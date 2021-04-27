package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.Puesto;

public interface IPuestoRepository extends JpaRepository<Puesto,Long>{
	
	@Query(value = "SELECT * FROM puesto WHERE estado='A'", nativeQuery=true)
    List<Puesto> obtenerActivos();

}
