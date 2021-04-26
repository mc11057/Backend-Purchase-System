package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.CategoriaEmpleado;

public interface ICategoriaEmpleadoService {

	 List<CategoriaEmpleado> findAll () throws Exception ;

	 CategoriaEmpleado findById(Long id) throws Exception;
}
