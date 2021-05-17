package com.ues.Purchases.service;

import java.util.List;

import com.ues.Purchases.model.FormaPago;

public interface IFormaPagoService {

	FormaPago findById(Long id) throws Exception;


	List<FormaPago> obtenerFormaPagoActivos() throws Exception;

}
