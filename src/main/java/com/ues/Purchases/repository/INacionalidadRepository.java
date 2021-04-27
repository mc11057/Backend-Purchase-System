package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.Nacionalidad;

public interface INacionalidadRepository extends JpaRepository<Nacionalidad,Long>{
	
	@Query(value = "SELECT * FROM nacionalidad WHERE estado='A'", nativeQuery=true)
    List<Nacionalidad> obtenerActivos();

}
