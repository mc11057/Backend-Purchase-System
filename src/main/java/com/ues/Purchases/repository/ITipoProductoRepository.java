package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.TipoProducto;

public interface ITipoProductoRepository extends JpaRepository<TipoProducto, Long> {
	
	@Query(value = "SELECT * FROM tipo_producto WHERE estado='A'", nativeQuery=true)
    List<TipoProducto> obtenerActivos();

}
