package com.ues.Purchases.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.Purchases.model.Region;
import com.ues.Purchases.service.IRegionService;
import com.ues.Purchases.utility.NotFoundException;

@RestController
@RequestMapping("/app/v1/region")
public class RegionController {

	@Autowired
	private IRegionService regionService;

	public RegionController(IRegionService regionService) {
		this.regionService = regionService;

	}

	@GetMapping()
	public ResponseEntity<List<Region>> getRegions() {

		List<Region> regiones = new ArrayList<Region>();
		try {
			regiones = (List<Region>) regionService.findAllRegions();
			return new ResponseEntity<List<Region>>(regiones, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Region> get(@PathVariable("id") Long id) {
		try {
			Region region = regionService.get(id);
			return new ResponseEntity<Region>(region, HttpStatus.OK);
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
