package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Empleado;

public interface IEmpleadoService {
	
	
	 List<Empleado> findAll() throws Exception ;

	 Empleado findById(Long id) throws Exception;
	 Empleado findByApplicationUser(Long id) throws Exception;

}
