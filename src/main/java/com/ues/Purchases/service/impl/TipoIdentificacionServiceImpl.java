package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.TipoIdentificacion;
import com.ues.Purchases.repository.ITipoIdentificacionRepository;
import com.ues.Purchases.service.ITipoIdentificacionService;

@Service
public class TipoIdentificacionServiceImpl implements ITipoIdentificacionService {
	
	@Autowired
	private ITipoIdentificacionRepository tipoIdentificacionRepository;

	@Override
	public List<TipoIdentificacion> findAll() throws Exception {
		// TODO Auto-generated method stub
		return tipoIdentificacionRepository.findAll();
	}

	@Override
	public TipoIdentificacion findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return tipoIdentificacionRepository.findById(id).orElse(null);
	}

	@Override
	public List<TipoIdentificacion> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return tipoIdentificacionRepository.obtenerActivos();
	}

}
