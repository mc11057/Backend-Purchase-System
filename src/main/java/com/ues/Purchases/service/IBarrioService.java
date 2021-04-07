package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Barrio;

public interface IBarrioService {

	
	 List<Barrio> findAllBarrio() throws Exception ;

	 Barrio findById(Long id) throws Exception;
	
	
}
