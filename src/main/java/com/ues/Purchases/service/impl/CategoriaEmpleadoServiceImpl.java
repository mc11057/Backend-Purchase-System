package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.CategoriaEmpleado;
import com.ues.Purchases.repository.ICategoriaEmpleadoRepository;
import com.ues.Purchases.service.ICategoriaEmpleadoService;

@Service
public class CategoriaEmpleadoServiceImpl implements ICategoriaEmpleadoService {
	
	@Autowired
	private ICategoriaEmpleadoRepository categoriaEmpleadoRepository;

	@Override
	public List<CategoriaEmpleado> findAll() throws Exception {
		// TODO Auto-generated method stub
		return categoriaEmpleadoRepository.findAll();
	}

	@Override
	public CategoriaEmpleado findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return categoriaEmpleadoRepository.findById(id).orElse(null);
	}

	@Override
	public List<CategoriaEmpleado> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return categoriaEmpleadoRepository.obtenerActivos();
	}

}
