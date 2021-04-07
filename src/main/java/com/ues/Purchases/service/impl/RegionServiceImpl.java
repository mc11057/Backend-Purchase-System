package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Region;
import com.ues.Purchases.repository.IRegionRepository;
import com.ues.Purchases.service.IRegionService;


@Service
public class RegionServiceImpl implements IRegionService {
	
	@Autowired
	private IRegionRepository regionRepository;

	@Override
	public List<Region> findAllRegions() throws Exception {
		return (List<Region>) regionRepository.findAll();
	}

	@Override
	public Region get(Long id) throws Exception {
		// TODO Auto-generated method stub
		return regionRepository.findById(id).orElse(null);
	}

}
