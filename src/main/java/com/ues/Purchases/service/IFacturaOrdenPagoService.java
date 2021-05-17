package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.FacturaOrdenPago;

public interface IFacturaOrdenPagoService {
	
	FacturaOrdenPago findById(Long id) throws Exception;


	List<FacturaOrdenPago> obtenerFacturaOrdenPagoActivos() throws Exception;

}
