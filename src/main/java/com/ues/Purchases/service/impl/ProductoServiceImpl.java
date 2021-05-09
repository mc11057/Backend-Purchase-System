package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Producto;
import com.ues.Purchases.repository.ICategoriaProductoRepository;
import com.ues.Purchases.repository.IProductoRepository;
import com.ues.Purchases.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoRepository productoRepository; 
	@Autowired
	private ICategoriaProductoRepository categoriaProductoRepository;
	
	public ProductoServiceImpl(IProductoRepository productoRepository,ICategoriaProductoRepository categoriaProductoRepository) {
		super();
		this.productoRepository = productoRepository;
		this.categoriaProductoRepository =categoriaProductoRepository;
	}
	

	@Override
	public List<Producto> findAll() throws Exception {
		return (List<Producto>)productoRepository.findAll();
	}

	@Override
	public Producto findById(Long id) throws Exception {
		return productoRepository.findById(id).orElse(null);
	}


	@Override
	public List<Producto> findByCategoriaProducto(Long categoriaProductoId) throws Exception {
		return productoRepository.findByCategoriaProducto(categoriaProductoRepository.findById(categoriaProductoId).get());

	}

}
