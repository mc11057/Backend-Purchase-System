package com.ues.Purchases.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.CategoriaProducto;
import com.ues.Purchases.model.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long>{
	
	
	List<Producto> findByCategoriaProducto(CategoriaProducto categoriaProducto);
	
}
