package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.TipoHorario;

public interface ITipoHorarioService {

	List<TipoHorario> findAllTipoHorario() throws Exception ;

	TipoHorario findById(Long id) throws Exception;
	
}
