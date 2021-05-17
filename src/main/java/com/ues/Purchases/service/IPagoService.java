package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.Pago;

public interface IPagoService {
	
	
	List<Pago> obtenerPagoActivos() throws Exception ;
	
	Pago findById(Long id) throws Exception;
	
	 void crearPago(Pago pago, String usuario) throws Exception;
	
	
	

}
