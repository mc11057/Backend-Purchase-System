package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Pago;
import com.ues.Purchases.repository.IPagoRepository;
import com.ues.Purchases.service.IPagoService;

@Service
public class PagoServiceImpl implements IPagoService {

	@Autowired
	private IPagoRepository pagoRepository;

	public PagoServiceImpl(IPagoRepository pagoRepository) {
		super();
		this.pagoRepository = pagoRepository;
	}

	@Override
	public List<Pago> obtenerPagoActivos() throws Exception {
		// TODO Auto-generated method stub
		return (List<Pago>) pagoRepository.obtenerPagoActivos();
	}

	@Override
	public Pago findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return pagoRepository.findById(id).orElse(null);
	}

	@Override
	public void guardarPagos(List<Pago> pagos) throws Exception { 
		
		for(Pago pago:pagos) 
		{
			pagoRepository.crear_pago(pago.getFacturaOrdenPagoId().getFacturaOrdenPagoId(),
					pago.getMonedaId().getMonedaId(), pago.getFormaPagoId().getFormaPagoId(), pago.getUserCreate(),pago.getMonto());
		}


	}

}
