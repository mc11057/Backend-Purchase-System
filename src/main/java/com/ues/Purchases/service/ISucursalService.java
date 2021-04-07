package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Sucursal;

public interface ISucursalService {

	List<Sucursal> findAllSucursal() throws Exception ;

	Sucursal findById(Long id) throws Exception;
	


}
