package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Horario;

public interface IHorarioService {
	
	List<Horario> findAllHorario() throws Exception ;

	Horario findById(Long id) throws Exception;
	

}
