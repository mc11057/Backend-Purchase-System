package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.CategoriaProducto;
import com.ues.Purchases.repository.ICategoriaProductoRepository;
import com.ues.Purchases.service.ICategoriaProductoService;

@Service
public class CategoriaProductoServiceImpl implements ICategoriaProductoService{
	
	@Autowired
	private ICategoriaProductoRepository categoriaProductoRepository;

	@Override
	public List<CategoriaProducto> findAll() throws Exception {
		// TODO Auto-generated method stub
		return categoriaProductoRepository.findAll();
	}

	@Override
	public CategoriaProducto findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return categoriaProductoRepository.findById(id).orElse(null);
	}

	@Override
	public List<CategoriaProducto> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return categoriaProductoRepository.obtenerActivos();
	}

}
