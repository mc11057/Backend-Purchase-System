package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.TipoProducto;
import com.ues.Purchases.repository.ITipoProductoRepository;
import com.ues.Purchases.service.ITipoProductoService;

@Service
public class TipoProductoServiceImpl implements ITipoProductoService {
	
	@Autowired
	private ITipoProductoRepository tipoProductoRepository;

	@Override
	public List<TipoProducto> findAll() throws Exception {
		// TODO Auto-generated method stub
		return tipoProductoRepository.findAll();
	}

	@Override
	public TipoProducto findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return tipoProductoRepository.findById(id).orElse(null);
	}

	@Override
	public List<TipoProducto> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return tipoProductoRepository.obtenerActivos();
	}

}
