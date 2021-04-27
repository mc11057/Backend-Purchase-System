package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.CategoriaProducto;

public interface ICategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long>{
	
	@Query(value = "SELECT * FROM categoria_producto WHERE estado='A'", nativeQuery=true)
    List<CategoriaProducto> obtenerActivos();

}
