package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.FacturaOrdenPago;
import com.ues.Purchases.repository.IFacturaOrdenPagoRepository;
import com.ues.Purchases.service.IFacturaOrdenPagoService;

@Service
public class FacturaOrdenPagoServiceImpl implements IFacturaOrdenPagoService{
	
	@Autowired
	private IFacturaOrdenPagoRepository facturaOrdenPagoRepository;
	

	@Override
	public FacturaOrdenPago findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return facturaOrdenPagoRepository.findById(id).orElse(null);
	}

	@Override
	public List<FacturaOrdenPago> obtenerFacturaOrdenPagoActivos() throws Exception {
		// TODO Auto-generated method stub
		return (List<FacturaOrdenPago>)facturaOrdenPagoRepository.obtenerFacturaOrdenPagoActivos();
	}

}
