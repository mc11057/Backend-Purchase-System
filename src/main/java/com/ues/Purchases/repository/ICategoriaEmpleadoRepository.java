package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.Purchases.model.CategoriaEmpleado;

public interface ICategoriaEmpleadoRepository extends JpaRepository<CategoriaEmpleado,Long> { 
	
	@Query(value = "SELECT * FROM categoria_empleado WHERE estado='A'", nativeQuery=true)
    List<CategoriaEmpleado> obtenerActivos();


}
