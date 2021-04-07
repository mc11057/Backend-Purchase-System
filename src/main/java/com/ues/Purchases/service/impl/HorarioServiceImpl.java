package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Horario;
import com.ues.Purchases.repository.IHorarioRepository;
import com.ues.Purchases.service.IHorarioService;

@Service
public class HorarioServiceImpl implements IHorarioService {
	
	@Autowired
	private IHorarioRepository horarioRepository;

	@Override
	public List<Horario> findAllHorario() throws Exception {
	
		return horarioRepository.findAll();
	}

	@Override
	public Horario findById(Long id) throws Exception {
		
		return horarioRepository.findById(id).orElse(null);
	}

}
