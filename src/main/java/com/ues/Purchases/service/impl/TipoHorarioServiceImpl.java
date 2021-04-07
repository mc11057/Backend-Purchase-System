package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.TipoHorario;
import com.ues.Purchases.repository.ITipoHorarioRepository;
import com.ues.Purchases.service.ITipoHorarioService;

@Service
public class TipoHorarioServiceImpl implements ITipoHorarioService {

	
	@Autowired
	private ITipoHorarioRepository tipoHorarioRepository;
	
	@Override
	public List<TipoHorario> findAllTipoHorario() throws Exception {
		// TODO Auto-generated method stub
		return tipoHorarioRepository.findAll();
	}

	@Override
	public TipoHorario findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return tipoHorarioRepository.findById(id).orElse(null);
	}
	
	

}
