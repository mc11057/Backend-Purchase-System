package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido,Long>{

}
