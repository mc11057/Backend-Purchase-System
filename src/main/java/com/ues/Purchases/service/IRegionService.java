package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Region;

public interface IRegionService {
	
	List<Region> findAllRegions() throws Exception;

	
	 Region get(Long id) throws Exception;
}
