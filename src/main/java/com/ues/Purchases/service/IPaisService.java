package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Pais;

public interface IPaisService {
	
	
	 List<Pais> findAllPais() throws Exception ;


	 Pais findById(Long id) throws Exception;

}
