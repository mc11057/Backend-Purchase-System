package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.Documento;

public interface IDocumentoRepository extends JpaRepository<Documento,Long> {

}
