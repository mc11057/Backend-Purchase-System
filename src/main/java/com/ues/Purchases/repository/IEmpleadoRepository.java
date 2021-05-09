package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.ApplicationUser;
import com.ues.Purchases.model.Empleado;

public interface IEmpleadoRepository extends JpaRepository<Empleado,Long> {
	
	
	 Empleado findByUser(ApplicationUser user) throws Exception;


}
