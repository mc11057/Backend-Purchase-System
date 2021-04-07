package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Departamento;

public interface IDepartamentoService {
	
	 List<Departamento> findAllDepartamento() throws Exception ;

	 Departamento findById(Long id) throws Exception;

}
