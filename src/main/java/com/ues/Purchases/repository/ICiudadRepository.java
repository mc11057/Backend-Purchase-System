package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.Ciudad;

public interface ICiudadRepository extends JpaRepository<Ciudad, Long> {

}
