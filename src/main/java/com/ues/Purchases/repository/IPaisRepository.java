package com.ues.Purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.Purchases.model.Pais;

public interface IPaisRepository extends JpaRepository<Pais, Long> {
	
	 //List<Pais> findByRegionId(Long regionId);
	 //Optional<Pais> findByIdAndRegionId(Long id, Long regionId);

}
