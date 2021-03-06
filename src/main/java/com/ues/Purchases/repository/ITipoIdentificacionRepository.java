package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.TipoIdentificacion;

public interface ITipoIdentificacionRepository extends JpaRepository<TipoIdentificacion,Long> {
	
	@Query(value = "SELECT * FROM tipo_identificacion WHERE estado='A'", nativeQuery=true)
    List<TipoIdentificacion> obtenerActivos();

}
